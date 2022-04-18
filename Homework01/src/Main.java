import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String winNum = sc.next();
        int n = sc.nextInt();
        int ans = 0;
        while ((n--)!=0){
            String mode = sc.next(), inp;
            if(!mode.equals("tractor")){
                inp = sc.next();
            }else {
                inp = "";
            }
//            System.out.println(mode+" "+inp);
            Lottery3D lottery3D;
            switch (mode){
                case "single":
                    lottery3D = new Single(winNum, inp);
                    break;
                case "group":
                    lottery3D = new Group(winNum, inp);
                    break;
                case "guess1d":
                    lottery3D = new Guess1d(winNum, inp);
                    break;
                case "oned":
                    lottery3D = new Oned(winNum, inp);
                    break;
                case "package":
                    lottery3D = new Package(winNum, inp);
                    break;
                case "sum":
                    lottery3D = new Sum(winNum, inp);
                    break;
                case "tractor":
                    lottery3D = new Tractor(winNum, inp);
                    break;
                case "general":
                    lottery3D = new General(winNum, inp);
                    break;
                default:
                    lottery3D = null;
            }
            assert lottery3D != null;
            ans = Math.max(ans, lottery3D.getWins());
        }
        System.out.println(ans);
    }
}

abstract class Lottery3D {
    protected Integer[] winNumber;
    protected Integer[] userNumber;
    public String userInput;

    Lottery3D(String winNumber) {
        this.winNumber = StringtoIntArray(winNumber);
    }

    Lottery3D(String winNumber, String userInput) {
        this(winNumber);
        this.userInput = userInput;
        setUserNumber(userInput);
    }

    public void setUserNumber(String userInput) {
        userNumber = StringtoIntArray(userInput);
    }

    public Integer[] getUserNumber() {
        return this.userNumber;
    }

    public abstract int getWins();

    Integer[] StringtoIntArray(String s) {
        Integer[] res = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            res[i] = Integer.parseInt(s.charAt(i) + "");
        }
        return res;
    }

    boolean isAvailable(String s) {
        return false;
    }

}

class Single extends Lottery3D {
    Single(String winNumber) {
        super(winNumber);
    }
    Single(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        if (Arrays.equals(winNumber, getUserNumber())) {
            return 1040;
        } else {
            return 0;
        }
    }
}

class Group extends Lottery3D {
    Group(String winNumber) {
        super(winNumber);
    }
    Group(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        Integer[] a = winNumber;
        Integer[] b = getUserNumber();
        Arrays.sort(a);
        Arrays.sort(b);
        if (Arrays.equals(a, b)) {
            if (Objects.equals(a[0], a[1])) {
                return 346;
            } else {
                return 173;
            }
        } else {
            return 0;
        }
    }
}

class Oned extends Lottery3D {
    Oned(String winNumber) {
        super(winNumber);
    }
    Oned(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        for (int i = 0; i < 3; i++) {
            if (Objects.equals(userNumber[i], winNumber[i])) {
                return 10;
            }
        }
        return 0;
    }

    @Override
    public void setUserNumber(String userInput) {
        userNumber = new Integer[3];
        for (int i = 0; i < userInput.length(); i++) {
            if (userInput.charAt(i) <= '9' && userInput.charAt(i) >= '0') {
                userNumber[i] = Integer.parseInt(userInput.charAt(i) + "");
            } else {
                userNumber[i] = 10;
            }
        }
    }
}

class Guess1d extends Lottery3D {
    Guess1d(String winNumber) {
        super(winNumber);
    }
    Guess1d(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public void setUserNumber(String userInput) {
        userNumber = new Integer[1];
        userNumber[0] = Integer.parseInt(userInput);
    }
    @Override
    public int getWins() {
        int sameNum = 0;
        for (int i = 0; i < 3; i++) {
            if (winNumber[i].equals(userNumber[0])) {
                sameNum++;
            }
        }
        if (sameNum == 1) {
            return 2;
        } else if (sameNum == 2) {
            return 12;
        } else if (sameNum == 3) {
            return 230;
        }
        return 0;
    }
}

class General extends Lottery3D {
    General(String winNumber) {
        super(winNumber);
    }
    General(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        if (Arrays.equals(winNumber, getUserNumber())) {
            return 470;
        } else {
            int sameNum = 0;
            for (int i = 0; i < 3; i++) {
                if (winNumber[i].equals(getUserNumber()[i])) {
                    sameNum++;
                }
            }
            if (sameNum == 2) {
                return 21;
            } else {
                return 0;
            }
        }
    }
}

class Sum extends Lottery3D {
    Sum(String winNumber) {
        super(winNumber);
    }
    Sum(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        int ans = 0;
        int sumWinNum = 0;
        for (int i = 0; i < 3; i++) {
            sumWinNum += winNumber[i];
        }
        if (sumWinNum == userNumber[0]) {
            switch (sumWinNum) {
                case 0:
                case 27:
                    ans = 1040;
                    break;
                case 1:
                case 26:
                    ans = (345);
                    break;
                case 2:
                case 25:
                    ans = (172);
                    break;
                case 3:
                case 24:
                    ans = (104);
                    break;
                case 4:
                case 23:
                    ans = (69);
                    break;
                case 5:
                case 22:
                    ans = (49);
                    break;
                case 6:
                case 21:
                    ans = (37);
                    break;
                case 7:
                case 20:
                    ans = (29);
                    break;
                case 8:
                case 19:
                    ans = (23);
                    break;
                case 9:
                case 18:
                    ans = (19);
                    break;
                case 10:
                case 17:
                    ans = (16);
                    break;
                case 11:
                case 16:
                case 12:
                case 15:
                    ans = (15);
                    break;
                case 13:
                case 14:
                    ans = (14);
                    break;
            }
        }
        //在此实现彩票金额返回
        return ans;
    }

    @Override
    public void setUserNumber(String userInput) {
        userNumber = new Integer[1];
        userNumber[0] = Integer.parseInt(userInput);
    }
}

class Package extends Lottery3D {
    Package(String winNumber) {
        super(winNumber);
    }
    Package(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        Integer[] u = userNumber.clone();
        Integer[] w = winNumber.clone();
        Arrays.sort(u);
        Arrays.sort(w);
        if (Arrays.equals(userNumber, winNumber)) {
            if (u[0].equals(u[1]) && u[1].equals(u[2])) {
                return 693;
            } else {
                return 606;
            }
        }
        if (Arrays.equals(u, w)) {
            if (u[0].equals(u[1]) && u[1].equals(u[2])) {
                return 173;
            } else {
                return 86;
            }
        }
        return 0;
    }
}

class Tractor extends Lottery3D {
    Tractor(String winNumber) {
        super(winNumber);
    }
    Tractor(String winNumber, String userInput) {
        super(winNumber, userInput);
    }
    @Override
    public int getWins() {
        if ((winNumber[0] == winNumber[1] + 1) && (winNumber[1] == winNumber[2] + 1)){
            return 65;
        }
        if((winNumber[0] == winNumber[1] - 1) && (winNumber[1] == winNumber[2] - 1)){
            return 65;
        }
        return 0;

    }
}