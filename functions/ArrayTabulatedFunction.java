package functions;

public class ArrayTabulatedFunction implements TabulatedFunction {
    private FunctionPoint[] Points;
    private int length;
    private final double EPSILON = 1e-9; //Машинный эпсилон для сравнений

    //Конструктор по количеству элементов
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {

            if (leftX > rightX) { //Проверка на соответствие левой и правой границы
                throw new IllegalArgumentException("Right border must be greater than left border");
            }

            if (pointsCount < 2){
                throw new IllegalArgumentException("There must be at least 2 points in tabulated function");
            }

            this.length = pointsCount;
            this.Points = new FunctionPoint[length];

            double delta = (rightX - leftX) / (length - 1); //Создание точек через равные по х промежутки
            for (int i = 0; i < pointsCount; i++) {
                double x = leftX + i * delta;
                this.Points[i] = new FunctionPoint(x, 0);
            }
        }

    //Конструктор по значениям
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {

        if (leftX > rightX) { //Проверка на соответствие левой и правой границы
            throw new IllegalArgumentException("Right border must be greater than left border");
        }

        if(values == null || values.length == 0) { // Защита от null и пустого массива
            throw new IllegalArgumentException("There must be at least 2 points in tabulated function");
        }

        this.Points =  new FunctionPoint[values.length];
        this.length = values.length;

        double delta = (rightX - leftX) / (length-1);
        for (int i = 0; i < length; i++){ //Создание точек через равные по х промежутки
            double x = leftX + i*delta;
            this.Points[i] = new FunctionPoint(x, values[i]);
        }
    }

    //Геттеры
    public double getLeftDomainBorder(){
        return this.Points[0].getX();
    }
    public double getRightDomainBorder(){
        return Points[length-1].getX();
    }
    public int getPointsCount(){
        return this.length;
    }

    public double getFunctionValue(double x){

        if ((x + EPSILON < this.getLeftDomainBorder()) || (x - EPSILON > this.getRightDomainBorder()))
            return Double.NaN; //x в пределах области функции

        for(int i = 0; i<length-1; i++){
            if (Points[i+1].getX() + EPSILON >= x) { //Ищем нужный отрезок между точками
                FunctionPoint p1 = Points[i];
                FunctionPoint p2 = Points[i+1];

                if(Math.abs(x-p1.getX()) <= EPSILON) //Если x совпадает с одной из точек-границ отрезка возвращаем по нему y
                    return p1.getY();

                if(Math.abs(x-p2.getX()) <= EPSILON)
                    return p2.getY();

                return p1.getY() + (x - p1.getX())* (p2.getY() - p1.getY()) / (p2.getX() - p1.getX()); //Считаем значение по формуле
            }
        }
        return Double.NaN;
    }

    public FunctionPoint getPoint(int index) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return new FunctionPoint(Points[index]);
    }

    public double getPointX(int index) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return this.Points[index].getX();
    }
    public double getPointY(int index) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        return this.Points[index].getY();
    }

    //Сеттеры
    public void setPoint(int index, FunctionPoint point)
        throws InappropriateFunctionPointException {
        if (index < 0 || index >= length || point == null)
            throw new FunctionPointIndexOutOfBoundsException(); //Проверка индекса
        //Проверка попадания в интервалы
        if (index>0 && point.getX() - EPSILON <= Points[index - 1].getX())
            throw new InappropriateFunctionPointException();
        Points[index] = new FunctionPoint(point);
    }

    public void setPointX(int index, double x)
        throws InappropriateFunctionPointException {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        //Проверка попадания в интервалы
        if ( index > 0 && x - EPSILON <= Points[index - 1].getX() ||
                (index < length - 1 && x >= Points[index + 1].getX())) {
            throw new InappropriateFunctionPointException();
        }

        Points[index].setX(x);
    }

    public void setPointY(int index, double y) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }
        Points[index].setY(y);
    }

    public void deletePoint(int index) {
        if(index<0 || index >= length) {
            throw new FunctionPointIndexOutOfBoundsException();
        }

        if (length < 3) {
            throw new IllegalStateException();
        }

        for (int i = index; i < length-1; i++) //Сдвигаем элементы влево
            Points[i] = Points[i+1];
        Points[length-1] = null; //Заменяем последний на пустой
        length--; //Уменьшаем переменную длины
    }

    public void addPoint(FunctionPoint point)
        throws InappropriateFunctionPointException {
        if (point == null) {
            throw new InappropriateFunctionPointException();
        }

        int index = 0; //Ищем место для новой точки по х
        while (index < length && point.getX() - EPSILON > Points[index].getX()) {
            index++;
        }
        if(Math.abs(point.getX() - Points[index].getX()) <= EPSILON) { //Если совпадает с другой точкой
            throw  new InappropriateFunctionPointException();
        }

        if (length == Points.length){ //Если массив первоначального размера, увеличиваем его
            FunctionPoint[] extendedPoints = new FunctionPoint[Points.length + 1];
            for (int i = 0; i< length; i++)
                extendedPoints[i] = Points[i];

            Points = extendedPoints;
        }

        for (int i=length; i> index; i--){ //Сдвиг элементов вправо
            Points[i] = Points[i-1];
        }
        Points[index] = new FunctionPoint(point); //Добавление элемента и увеличение переменной длины
        length++;
    }

}