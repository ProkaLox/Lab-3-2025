import functions.*;

public class Main {
    public static void main(String[] args) throws InappropriateFunctionPointException {
        // Демонстрация линейной функции y = 3x + 3
        System.out.println("----- Линейная функция y = 3x+3 -----");
        TabulatedFunction linFunction = new ArrayTabulatedFunction(0, 10, 6);
        for (int i = 0; i < linFunction.getPointsCount(); i++){
            double x = linFunction.getPointX(i);
            linFunction.setPointY(i, 3 * x + 3);
        }

        System.out.println("Исходная функция:");
        for (int i = 0; i < linFunction.getPointsCount(); i++) {
            System.out.println("Точка " + i + ": x=" + linFunction.getPointX(i) + " y=" + linFunction.getPointY(i));
        }

        // Демонстрация добавления точки
        System.out.println("\n----- Добавление точки (3; 12) -----");
        linFunction.addPoint(new FunctionPoint(3.0, 12.0));
        System.out.println("После добавления:");
        for (int i = 0; i < linFunction.getPointsCount(); i++) {
            System.out.println("Точка " + i + ": x=" + linFunction.getPointX(i) + " y=" + linFunction.getPointY(i));
        }

        // Демонстрация удаления точки
        System.out.println("\n----- Удаление точки с индексом 3 -----");
        linFunction.deletePoint(3);
        System.out.println("После удаления:");
        for (int i = 0; i < linFunction.getPointsCount(); i++) {
            System.out.println("Точка " + i + ": x=" + linFunction.getPointX(i) + " y=" + linFunction.getPointY(i));
        }

        // Демонстрация изменения точки
        System.out.println("\n----- Изменение точки с индексом 4 -----");
        linFunction.setPoint(4, new FunctionPoint(7.0, 24.0));
        System.out.println("После изменения:");
        for (int i = 0; i < linFunction.getPointsCount(); i++) {
            System.out.println("Точка " + i + ": x=" + linFunction.getPointX(i) + " y=" + linFunction.getPointY(i));
        }

        // Демонстрация интерполяции
        System.out.println("\n----- Пример интерполяции -----");
        System.out.printf("getFunctionValue(%.1f) = %.2f (3*%.1f + 3)%n",  3.5, linFunction.getFunctionValue(3.5), 3.5);
        System.out.printf("getFunctionValue(%.1f) = %.2f (3*%.1f + 3)%n",  6.8, linFunction.getFunctionValue(6.8), 6.8);

        // Демонстрация границ области определения
        System.out.println("\n----- Границы функции -----");
        System.out.println("Левая граница: " + linFunction.getLeftDomainBorder());
        System.out.println("Правая граница: " + linFunction.getRightDomainBorder());

        // Демонстрация работы с отдельными координатами
        System.out.println("\n----- Изменение координат по отдельности -----");
        System.out.println("До изменения:");
        System.out.println("Точка 0: x=" + linFunction.getPointX(0) + " y=" + linFunction.getPointY(0));

        linFunction.setPointX(0, 0.5);
        linFunction.setPointY(0, 2.0);

        System.out.println("После изменения:");
        System.out.println("Точка 0: x= " + linFunction.getPointX(0) + " y= " + linFunction.getPointY(0));


        System.out.println("\n----- Функция после всех операций -----");
        for (int i = 0; i < linFunction.getPointsCount(); i++) {
            System.out.println("Точка " + i + ": x=" + linFunction.getPointX(i) + " y=" + linFunction.getPointY(i));
        }
        System.out.println("Всего точек: " + linFunction.getPointsCount());


        // ДЕМОНСТРАЦИЯ ИСКЛЮЧЕНИЙ
        System.out.println("\n\n----- Демонстрация исключений -----");

        // 1. Некорректный конструктор
        System.out.println("1) Тест некорректного конструктора:");
        System.out.print("=ArrayTabulatedFunction: ");
        try {
            TabulatedFunction errorFunction = new ArrayTabulatedFunction(10, 5, 5);
            System.out.println("Все хорошо");
        } catch (IllegalArgumentException error) {
            System.out.println("Исключение: IllegalArgumentException");
        }
        System.out.print("=LinkedListTabulatedFunction: ");
        try {
            TabulatedFunction errorFunction = new LinkedListTabulatedFunction(10, 5, 5);
            System.out.println("Все хорошо");
        } catch (IllegalArgumentException error) {
            System.out.println("Исключение: IllegalArgumentException");
        }
        // 2. Выход за границы индекса
        System.out.println("\n2) Тест выхода за границы индекса в getPoint():");
        try {
            linFunction.getPoint(100);
            System.out.println("Все в порядке");
        } catch (FunctionPointIndexOutOfBoundsException error) {
            System.out.println("Исключение: FunctionPointIndexOutOfBoundsException");
        }
        // System.out.println("x=" + linFunction.getPointX(2) +" y=" + linFunction.getPointY(2));
        // 3. Нарушение упорядоченности при установке точки
        System.out.println("\n3) Тест нарушения упорядоченности в setPoint:");
        try {
            linFunction.setPoint(2, new FunctionPoint(2, 5.0)); // Должно быть между 2 и 3
            System.out.println("Все круто");
        } catch (InappropriateFunctionPointException error) {
            System.out.println("Исключение: InappropriateFunctionPointException");
        }

        // 4. Добавление точки с существующей X
        System.out.println("\n4) Тест addPoint() с уже существующим X:");
        try {
            linFunction.addPoint(new FunctionPoint(6.0, 25.0)); // X=6.0 уже существует
            System.out.println("Все великолепно");
        } catch (InappropriateFunctionPointException error) {
            System.out.println("Исключение: InappropriateFunctionPointException");
        }

        // 5. Удаление точки при недостаточном количестве точек
        System.out.println("\n5) Тест deletePoint() при малом количестве точек:");
        try {
            TabulatedFunction smallFunc = new ArrayTabulatedFunction(0, 1, 2);
            smallFunc.deletePoint(0);
            System.out.println("Все замечательно");
        } catch (IllegalStateException error) {
            System.out.println("Исключение IllegalStateException");
        }

        // 6. Некорректный индекс при удалении
        System.out.println("\n6) Тест некорректного индекса в deletePoint():");
        try {
            linFunction.deletePoint(-1);
            System.out.println("Все невероятно");
        } catch (FunctionPointIndexOutOfBoundsException error) {
            System.out.println("Исключение: FunctionPointIndexOutOfBoundsException");
        }

        // 7. Нарушение упорядоченности при изменении X
        System.out.println("\n7) Тест нарушения упорядоченности при setPointX():");
        try {
            linFunction.setPointX(1, 4.0); // Должно быть между 0.5 и 3
            System.out.println("Все устраивает");
        } catch (InappropriateFunctionPointException error) {
            System.out.println("Исключение: InappropriateFunctionPointException");
        }

        // 8. Создание функции с недостаточным количеством точек
        System.out.println("\n8) Тест создания функции с 1 точкой:");
        try {
            TabulatedFunction errorFunction = new LinkedListTabulatedFunction(0, 5, 1);
            System.out.println("Все как обычно");
        } catch (IllegalArgumentException error) {
            System.out.println("Исключение: IllegalArgumentException");
        }

        // 9. Некорректный индекс при получении Y
        System.out.println("\n9) Тест некорректного индекса при getPointY:");
        try {
            linFunction.getPointY(100);
            System.out.println("НИЧЕГО НЕ ПРОИЗОШЛО");
        } catch (FunctionPointIndexOutOfBoundsException error) {
            System.out.println("Исключение: FunctionPointIndexOutOfBoundsException");
        }


    }
}

