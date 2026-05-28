package jdk13;

public class _01_switch表达式写法可以有返回值 {

    public static void main(String[] args) {
        int month = 3;
        switch (month) {
            case 2:
            case 3:
            case 5:
                System.out.println("spring");
                break;
            case 6:
            case 7:
            case 8:
                System.out.println("summer");
                break;
            case 9:
            case 10:
            case 11:
                System.out.println("autumn");
                break;
            case 12:
                System.out.println("winter");

            default:
                System.out.println("wrong");
        }

        // jdk12: 表达式写法
        switch (month) {
            case 2, 3, 4 -> {
                System.out.println("spring");
            }
            case 5, 6, 7 -> {
                System.out.println("summer");
            }
            default -> {
                System.out.println("wrong");
            }
        }

        // jdk13
        String s = switch (month) {
            case 2, 3, 4 -> "spring";
            case 5, 6, 7 -> "summer";
            default -> "wrong";
        };
    }
}
