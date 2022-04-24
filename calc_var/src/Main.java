import javax.swing.plaf.InsetsUIResource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.*;

//定义异常种类
class WrongExpression extends Exception {
    public WrongExpression() {
    }

    public WrongExpression(String message) {
        super(message);
    }
}

class WrongCalc extends Exception {
    public WrongCalc() {
    }

    public WrongCalc(String message) {
        super(message);
    }
}

class VarUndefined extends Exception {
    public VarUndefined() {
    }

    public VarUndefined(String message) {
        super(message);
    }
}

class VarUnassigned extends Exception {
    public VarUnassigned() {
    }

    public VarUnassigned(String message) {
        super(message);
    }
}

class Calc {
    String toCalc;
    String suffix;
    boolean hasFloat;
    HashMap<Character, Integer> hashMap = new HashMap<>();

    /**
     * 判断是否为数字
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 进行初始化操作
     *
     * @param toCalc 待计算的表达式
     * @throws Exception
     */
    public Calc(String toCalc) throws Exception {
        this.toCalc = toCalc;
        hashMap.put('+', 1);
        hashMap.put('-', 1);
        hashMap.put('*', 2);
        hashMap.put('/', 2);
        hashMap.put('%', 2);
        for (var i :
                Main.nums.values()) {
            if ("float".equals(i.typeName)) {
                hasFloat = true;
                break;
            }
        }
        suffix = toSuffix();


    }

    /**
     * 转化成后缀表达式
     */
    private String toSuffix() throws Exception {
        StringBuilder sb = new StringBuilder();
        Stack<Character> stack = new Stack<Character>();
        Logger logger = Logger.getLogger(Calc.class.getName());
//        System.out.println(toCalc);
        toCalc = toCalc.substring(0, toCalc.length() - 2);
        outer:
        for (int i = 0; i < toCalc.length(); i++) {
            char c = toCalc.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (c == '-') {
                if (i == 0) {
                    sb.append('0');
                    sb.append(' ');
                } else if (toCalc.charAt(i - 1) == '(') {
                    sb.append('0');
                    sb.append(' ');
                }
            }
            if (hashMap.get(c) == null && c != '(' && c != ')') {
                StringBuilder temp = new StringBuilder();
                while (hashMap.get(c) == null && c != '(' && c != ')') {
                    temp.append(c);
                    i++;
                    if (i >= toCalc.length()) {
                        sb.append(temp.toString()).append(" ");
                        break outer;
                    }
                    c = toCalc.charAt(i);
                }
//                Number value = Main.nums.get(temp.toString()).value;
                sb.append(temp.toString()).append(" ");
                c = toCalc.charAt(i);
                if (c == ' ') {
                    continue;
                }
            }
            if (hashMap.get(c) != null || c == '(' || c == ')') {
                if (stack.empty()) {
                    stack.push(c);
                } else if (c == '(') {
                    stack.push('(');
                } else if (c == ')') {
                    while (!stack.empty() && stack.peek() != '(') {
                        sb.append(stack.peek());
                        stack.pop();
                        sb.append(' ');
                        if (stack.empty()) {
                            throw new WrongExpression();
                        }
                    }
                    stack.pop();
                } else if (hashMap.get(c) == 1) {
                    while (!stack.empty() && stack.peek() != '(' && hashMap.get(stack.peek()) >= 1) {
                        sb.append(stack.peek());
                        sb.append(' ');
                        stack.pop();
                    }
                    stack.push(c);
                } else if (hashMap.get(c) == 2) {
                    while (!stack.empty() && stack.peek() != '(' && hashMap.get(stack.peek()) >= 2) {
                        sb.append(stack.peek());
                        sb.append(' ');
                        stack.pop();
                    }
                    stack.push(c);
                }
            }
        }
        while (!stack.empty()) {
            sb.append(' ');
            if (stack.peek() != '+' && stack.peek() != '-' && stack.peek() != '*' && stack.peek() != '/' && stack.peek() != '%') {
                throw new WrongExpression();
            }
            sb.append(stack.pop());
        }
        logger.log(Level.INFO,"change the expression to the suffix\nsuffix: "+sb.toString());
        return sb.toString();
    }

    /**
     * 进行计算
     */
    public String calcRes() throws Exception {
        Stack<Num> nums = new Stack<Num>();
        String s = suffix;
        out:
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            }
            StringBuilder temp = new StringBuilder();
            boolean hasPoint = false;
            while (c != '+' && c != '-' && c != '*' && c != '/' && c != '%') {
                if (c == '.') {
                    hasPoint = true;
                }
                temp.append(c);
                i++;
                if (i >= s.length()) {
                    break out;
                }
                c = s.charAt(i);
                if (c == ' ') break;
            }
            if (temp.length() > 0) {
                if (isNumeric(temp.toString())) {
                    nums.push(hasPoint ? new Num("float", Float.parseFloat(temp.toString())) :
                            new Num("int", Integer.parseInt(temp.toString())));
                } else if (Main.nums.get(temp.toString()) == null) {
                    throw new VarUndefined();
                } else {
                    nums.push(Main.nums.get(temp.toString()));
                }
            }


            if (c == '+') {
                if (nums.size() < 2) {
                    throw new WrongExpression();
                }
                Num temp1 = nums.pop();
                Num temp2 = nums.pop();
                nums.push(temp2.plus(temp1));
            }
            if (c == '-') {
                if (nums.size() < 2) {
                    throw new WrongExpression();
                }
                Num temp1 = nums.pop();
                Num temp2 = nums.pop();
                nums.push(temp2.minus(temp1));
            }
            if (c == '*') {
                if (nums.size() < 2) {
                    throw new WrongExpression();
                }
                Num temp1 = nums.pop();
                Num temp2 = nums.pop();
                nums.push(temp2.times(temp1));
            }
            if (c == '/') {
                if (nums.size() < 2) {
                    throw new WrongExpression();
                }
                Num temp1 = nums.pop();
                Num temp2 = nums.pop();
                if (temp1.value.equals(0)) {
                    throw new WrongExpression();
                }
                nums.push(temp2.divide(temp1));
            }
            if (c == '%') {
                if (nums.size() < 2) {
                    throw new WrongExpression();
                }
                Num temp1 = nums.pop();
                Num temp2 = nums.pop();
                if (temp1.value.equals(0)) {
                    throw new WrongExpression();
                }
                nums.push(temp2.mod(temp1));
            }
        }
        if (nums.size() != 1) {
            throw new WrongExpression();
        }
        if ("float".equals(nums.peek().typeName)) {
            return String.format("%.2f", (float) nums.peek().value);
        }
        return nums.peek().value.toString();
    }
}

class Num {
    String typeName;
    Number value;
    Logger logger = Logger.getLogger(Num.class.getName());

    public Num(String typeName) {
        this.typeName = typeName;
        this.value = null;
    }

    public Num(String typeName, Number value) {
        this.typeName = typeName;
        this.value = value;
    }

    Num plus(Num n) throws VarUnassigned {
        Num res;
        if (this.value == null || n.value == null) {
            throw new VarUnassigned();
        }
        if ("float".equals(this.typeName) || "float".equals(n.typeName)) {
            res = new Num("float", this.value.floatValue() + n.value.floatValue());
        } else {
            res = new Num("int", this.value.intValue() + n.value.intValue());
        }
        logger.log(Level.INFO, "calc: " + this.value + " + " + n.value + "\nans=" + res.value);
        return res;
    }

    Num minus(Num n) throws VarUnassigned {
        logger.log(Level.INFO, "calc: " + this.value + " - " + n.value);
        Num res;
        if (this.value == null || n.value == null) {
            throw new VarUnassigned();
        }
        if ("float".equals(this.typeName) || "float".equals(n.typeName)) {
            res = new Num("float", this.value.floatValue() - n.value.floatValue());
        } else {
            res = new Num("int", this.value.intValue() - n.value.intValue());
        }
        logger.log(Level.INFO, "calc: " + this.value + " - " + n.value + "\nans=" + res.value);
        return res;
    }

    Num times(Num n) throws VarUnassigned {
        Num res;
        if (this.value == null || n.value == null) {
            throw new VarUnassigned();
        }
        if ("float".equals(this.typeName) || "float".equals(n.typeName)) {
            res = new Num("float", this.value.floatValue() * n.value.floatValue());
        } else {
            res = new Num("int", this.value.intValue() * n.value.intValue());
        }
        logger.log(Level.INFO, "calc: " + this.value + " * " + n.value + "\nans=" + res.value);
        return res;
    }

    Num divide(Num n) throws VarUnassigned {
        Num res;
        if (this.value == null || n.value == null) {
            throw new VarUnassigned();
        }
        if ("float".equals(this.typeName) || "float".equals(n.typeName)) {
            res = new Num("float", this.value.floatValue() / n.value.floatValue());
        } else {
            res = new Num("int", this.value.intValue() / n.value.intValue());
        }
        logger.log(Level.INFO, "calc: " + this.value + " / " + n.value + "\nans=" + res.value);
        return res;
    }

    Num mod(Num n) throws VarUnassigned {

        Num res;
        if (this.value == null || n.value == null) {
            throw new VarUnassigned();
        }
        if ("float".equals(this.typeName) || "float".equals(n.typeName)) {
            res = new Num("int", (int) this.value.floatValue() % (int) n.value.floatValue());
        } else {
            res = new Num("int", this.value.intValue() % n.value.intValue());
        }
        logger.log(Level.INFO, "calc: " + this.value + " % " + n.value + "\nans=" + res.value);
        return res;
    }
}

public class Main {
    public static Logger logger = Logger.getLogger(Main.class.getName());
    public static HashMap<String, Num> nums = new HashMap<>();




    public static void main(String[] args) {
        Scanner sc = null;
        Logger logger = Logger.getLogger(Main.class.getName());
        String logfile = "./log.txt";
        Handler fh = null;
        try {
            fh = new FileHandler("./log.txt");
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fh != null;
        fh.setLevel(Level.INFO);
        Handler ch = new ConsoleHandler();
        ch.setLevel(Level.WARNING);
        logger.addHandler(ch);


//        Handler handler = new ConsoleHandler();
//        logger.addHandler(handler);
        try {
            sc = new Scanner(new File("./1.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        sc = new Scanner(System.in);
        assert sc != null;
        while (sc.hasNextLine()) {
            String cmd = sc.nextLine();
            if (!sc.hasNextLine()) {
                logger.log(Level.INFO, "get the expression: "+cmd);
                Calc calc;
                try {
                    calc = new Calc(cmd);
                    System.out.println(calc.calcRes());
                } catch (VarUndefined e) {
                    logger.log(Level.SEVERE, "wrong - variable undefined");
                } catch (VarUnassigned e) {
                    logger.log(Level.SEVERE, "wrong - variable unassigned");
                } catch (WrongExpression e) {
                    logger.log(Level.SEVERE, "wrong - error expression");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            ArrayList<Integer> semiPos = new ArrayList<>();
            semiPos.add(-1);
            ArrayList<String> subcmds = new ArrayList<>();
            for (int i = 0; i < cmd.length(); i++) {
                if (cmd.charAt(i) == ';') {
                    semiPos.add(i);
                }
            }
            for (int i = 0; i < semiPos.size() - 1; i++) {
                subcmds.add(cmd.substring(semiPos.get(i) + 1, semiPos.get(i + 1)));
            }
            for (var subcmd : subcmds) {
                logger.log(Level.INFO, "get the command: "+subcmd+";");
                String name = null, type = null;
                String value = null;
                int eqlPos = -1;
                int pointPos = -1;
                int intPos = subcmd.indexOf("int");
                int floatPos = subcmd.indexOf("float");
                int doublePos = subcmd.indexOf("double");
                int nameStart = 0;
                for (int i = 0; i < subcmd.length(); i++) {
                    if (subcmd.charAt(i) == '=') {
                        eqlPos = i;
                    }
                    if (subcmd.charAt(i) == '.') {
                        pointPos = i;
                    }

                }
                if (intPos != -1) {
                    type = "int";
                    nameStart = 4;
                }
                if (floatPos != -1) {
                    type = "float";
                    nameStart = 6;
                }
                if (eqlPos != -1) {
                    name = subcmd.substring(nameStart, eqlPos);
                    value = subcmd.substring(eqlPos + 1);
                } else {
                    name = subcmd.substring(nameStart);
                }
//                System.out.println(name);
//                System.out.println(type);
//                System.out.println(value);
                if (type != null) {
                    logger.log(Level.INFO, "declared the variable: " + name);
                    nums.put(name, new Num(type));
                }
                if (value != null) {
                    Number trueValue;
                    if (pointPos != -1) {
                        trueValue = Float.parseFloat(value);
                    } else {
                        trueValue = Integer.parseInt(value);
                    }
                    logger.log(Level.INFO, "the variable "+name+" is assigned the value of " + trueValue);
                    nums.get(name).value = trueValue;
                }
            }
        }
    }
}
