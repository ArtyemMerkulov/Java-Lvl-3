package homework5.classes;

import homework5.exceptions.OperationException;
import homework5.interfaces.Operationable;

import java.util.Scanner;

public class Calculator {

    private double result;
    private boolean hasExit;

    private final Scanner sc;
    private final Operationable[] operations = new Operationable[] {
            (param1, param2) -> param1 + param2,
            (param1, param2) -> param1 - param2,
            (param1, param2) -> param1 * param2,
            (param1, param2) -> param1 / param2
    };

    public Calculator() {
        hasExit = false;
        sc = new Scanner(System.in);

        int operation = 5;
        double param;
        boolean isUnknownOperation;

        initResult();

        while (!hasExit) {
            if (operation == 5) {
                result = getParameter();
                printResult();
            }

            do {
                isUnknownOperation = false;

                printOperationMenu();

                try {
                    operation = getOperation();

                    if (operation >= 1 && operation <= 6) {
                        if (operation <= 4) {
                            param = getParameter();

                            if (operation == 4 && param == 0) throw new ArithmeticException("Result undefined");

                            result = operations[operation - 1].calculate(result, param);
                            printResult();
                        } else if (operation == 5) initResult();
                        else exit();
                    } else throw new OperationException(operation);
                } catch (ArithmeticException e) {
                    System.out.println("Result undefined");
                    initResult();
                    operation = 5;
                } catch (Exception e) {
                    System.out.println("Unknown operation");
                    isUnknownOperation = true;
                }
            } while (isUnknownOperation);
        }
    }

    private double getParameter() {
        System.out.print("Enter number: ");
        return Double.parseDouble(sc.nextLine());
    }

    private int getOperation() {
        System.out.print("Number of operation: ");
        return Integer.parseInt(sc.nextLine());
    }

    private void initResult() {
        result = 0.;
        printResult();
    }

    private void exit() {
        sc.close();
        hasExit = true;
    }

    private void printResult() {
        System.out.println("Current result: " + result);
    }

    private void printOperationMenu() {
        System.out.print("\nEnter number of operation:\n" +
                "1. sum;\n" +
                "2. sub;\n" +
                "3. mul;\n" +
                "4. div;\n" +
                "5. clear result;\n" +
                "6. exit.\n");
    }
}
