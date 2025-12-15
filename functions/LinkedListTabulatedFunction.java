package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private final double EPSILON = 1e-9;
    private static class FunctionNode {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;

        FunctionNode(FunctionPoint point) { //Конструктор узла по заданной точке
            this.point = point;
        }

        public FunctionNode(){ //Конструктор для создания головы
            this.prev = this;
            this.next = this;
        }


    }
    private FunctionNode head = new FunctionNode();
    private int length;

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX >= rightX) { //Проверка на соответствие левой и правой границы
            throw new IllegalArgumentException("Right border must be greater than left border");
        }

        if (pointsCount < 2){
            throw new IllegalArgumentException("There must be at least 2 points in tabulated function");
        }

        double delta = (rightX - leftX) / (pointsCount - 1); //Создание точек через равные по х промежутки
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * delta;
            FunctionPoint element = new FunctionPoint(x,0);
            addNodeToTail(element);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {

        if (leftX >= rightX) { //Проверка на соответствие левой и правой границы
            throw new IllegalArgumentException("Right border must be greater than left border");
        }

        if(values == null || values.length < 2) { // Защита от null и пустого массива
            throw new IllegalArgumentException("There must be at least 2 points in tabulated function");
        }

        int pointsCount = values.length;

        double delta = (rightX - leftX) / (pointsCount-1);
        for (int i = 0; i < pointsCount; i++){ //Создание точек через равные по х промежутки
            double x = leftX + i*delta;
            FunctionPoint element = new FunctionPoint(x,values[i]);
            addNodeToTail(element);
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        FunctionNode currentElement;

        if (index < length / 2) { //Выбираем сторону поиска для оптимизации
            currentElement = head.next;
            for (int i = 0; i < index; i++) {
                currentElement = currentElement.next;
            }
        }
        else {
            currentElement = head.prev;
            for (int i = length - 1; i > index; i--) {
                currentElement = currentElement.prev;
            }
        }
        return currentElement;
    }

    FunctionNode addNodeToTail(FunctionPoint point){
        length++;
        FunctionNode newPoint = new FunctionNode(point);
        FunctionNode tail = head.prev;

        tail.next = newPoint;
        head.prev = newPoint;

        newPoint.prev = tail;
        newPoint.next = head;

        return  newPoint;
    }

    FunctionNode addNodeByIndex(int index, FunctionPoint point){
        FunctionNode targetPosition = getNodeByIndex(index);
        FunctionNode newPoint = new FunctionNode(point);
        length++;

        newPoint.prev = targetPosition.prev;
        newPoint.next = targetPosition;

        targetPosition.prev.next = newPoint;
        targetPosition.prev = newPoint;

        return newPoint;
    }

    FunctionNode deleteNodeByIndex(int index){
        FunctionNode deletedPoint = getNodeByIndex(index);
        length--;

        deletedPoint.prev.next = deletedPoint.next;
        deletedPoint.next.prev = deletedPoint.prev;

        return deletedPoint;
    }

    public double getLeftDomainBorder(){
        return head.next.point.getX();
    }

    public double getRightDomainBorder(){
        return head.prev.point.getX();
    }

    public int getPointsCount(){
        return length;
    }

    public double getFunctionValue(double x){
        if ((x + EPSILON < getLeftDomainBorder()) || (x - EPSILON > getRightDomainBorder()))
            return Double.NaN; //x в пределах области функции

        FunctionNode current = head.next;

        while (current != head.prev){
            FunctionPoint p1 = current.point;
            FunctionPoint p2 = current.next.point;

            if (p2.getX() + EPSILON >= x) {
                if (Math.abs(x - p1.getX()) <= EPSILON) //Если x совпадает с одной из точек-границ отрезка возвращаем по ней y
                    return p1.getY();

                if (Math.abs(x - p2.getX()) <= EPSILON)
                    return p2.getY();

                return p1.getY() + (x - p1.getX()) * (p2.getY() - p1.getY()) / (p2.getX() - p1.getX()); //Считаем значение по формуле
            }
            current = current.next;
        }
        return Double.NaN;
    }

    public FunctionPoint getPoint(int index){
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public double getPointX(int index) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return this.getNodeByIndex(index).point.getY();
    }

    public void setPoint(int index, FunctionPoint point)
            throws InappropriateFunctionPointException {
        if (index < 0 || index >= length || point == null) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        // Проверка по предыдущему узлу
        if (index > 0) {
            FunctionNode prevNode = getNodeByIndex(index - 1);
            if (point.getX() - EPSILON <= prevNode.point.getX()) {
                throw new InappropriateFunctionPointException();
            }
        }

        getNodeByIndex(index).point = new FunctionPoint(point);
    }

    public void setPointX(int index, double x)
            throws InappropriateFunctionPointException {
        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        //Проверка попадания в интервалы
        if ((index > 0 && x <= getNodeByIndex(index - 1).point.getX()) ||
                (index < length - 1 && x >= getNodeByIndex(index + 1).point.getX())) {
            throw new InappropriateFunctionPointException();
        }

        getNodeByIndex(index).point.setX(x);
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        if (length < 3) {
            throw new IllegalStateException();
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point)
            throws InappropriateFunctionPointException {
        if (point == null) {
            throw new  InappropriateFunctionPointException();
        }

        FunctionNode current = head.next;
        while (current != head) {
            if (Math.abs(current.point.getX() - point.getX()) < EPSILON) { //Если совпадает с другой точкой
                throw new InappropriateFunctionPointException();
            }
            current = current.next;
        }

        current = head.next;
        int newIndex = 0; //Ищем место для новой точки по х

        while (current != head && point.getX() > current.point.getX()) {
            current = current.next;
            newIndex++;
        }

        addNodeByIndex(newIndex, point);
    }
}
