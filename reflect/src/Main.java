import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class Student {
    public String stdname;
    public Integer stdcode;

    public Student(String stdname, Integer stdcode) {
        this.stdname = stdname;
        this.stdcode = stdcode;
    }

    public Student(String stdname) {
        this.stdname = stdname;
        this.stdcode = new Random().nextInt();
    }

    public void study(String lesson) {
        System.out.println(stdname + " learning " + lesson);
    }

    public void setName(String stdname) {
        this.stdname = stdname;
    }

    public void doSomeHelp(Student s) {
        System.out.println(stdname + " help " + s.stdname);
    }
}

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        HashMap<String, Student> students = new HashMap<>();
        Scanner sc = new Scanner(System.in);
//        System.out.println("0123456".substring(0,5));
        while (sc.hasNextLine()) {
            int equalPos = -1, pointPos = -1,
                    leftBracketPos = -1, rightBracketPos = -1,
                    commaPos = -1, blankPos = -1, newPos = -1;
            String cmd = sc.nextLine();
            for (int i = 0; i < cmd.length(); i++) {
                if (cmd.charAt(i) == ' ' && blankPos == -1) {
                    blankPos = i;
                }
                if (cmd.charAt(i) == '=') {
                    equalPos = i;
                }
                if (cmd.charAt(i) == '.') {
                    pointPos = i;
                }
                if (cmd.charAt(i) == '(') {
                    leftBracketPos = i;
                }
                if (cmd.charAt(i) == ')') {
                    rightBracketPos = i;
                }
                if (cmd.charAt(i) == ',') {
                    commaPos = i;
                }
                if (i > 3) {
                    if (cmd.charAt(i) == ' ' && cmd.charAt(i - 1) == 'w' && cmd.charAt(i - 2) == 'e' && cmd.charAt(i - 3) == 'n') {
                        newPos = i;
                    }
                }
            }
            if (equalPos >= 0) {
                String typeName = cmd.substring(newPos + 1, leftBracketPos);
//                System.out.println(typeName);
                String name;
                if (equalPos < blankPos) {
                    name = cmd.substring(0, equalPos);
                } else {
                    name = cmd.substring(blankPos + 1, equalPos);
                }

//                System.out.println(name);
                String cmdArgs = cmd.substring(leftBracketPos + 1, rightBracketPos);
//                System.out.println(cmdArgs);

                if (commaPos >= 0) {
                    String arg1 = cmd.substring(leftBracketPos + 1, commaPos);
                    int arg2 = Integer.parseInt(cmd.substring(commaPos + 1, rightBracketPos));
//                    System.out.println("arg1:" + arg1);
//                    System.out.println("arg2:" + arg2);
                    if (arg1.charAt(0) == '\"' && arg1.charAt(arg1.length() - 1) == '\"') {
                        arg1 = arg1.substring(1, arg1.length() - 1);
                    }
                    try {
                        Object obj = Class.forName(typeName)
                                .getConstructor(String.class, Integer.class)
                                .newInstance(arg1, arg2);
                        students.put(name, (Student) obj);
                    } catch (ClassNotFoundException e) {
                        System.out.println("Wrong Type");
                    } catch (NoSuchMethodException e) {
                        System.out.println("Wrong Constructor");
                    } catch (Exception e) {
                        System.out.println("Wrong Statement");
                    }
                } else {
                    String arg1 = cmd.substring(leftBracketPos + 1, rightBracketPos);
                    Class argType = null;
                    if (arg1.length() > 0 && arg1.charAt(0) == '\"' && arg1.charAt(arg1.length() - 1) == '\"') {
                        arg1 = arg1.substring(1, arg1.length() - 1);
                        argType = String.class;
                    }

//                    System.out.println("arg1:" + arg1);
                    try {
                        Object obj = Class.forName(typeName)
                                .getConstructor(argType)
                                .newInstance(arg1);
                        students.put(name, (Student) obj);
                    } catch (ClassNotFoundException e) {
                        System.out.println("Wrong Type");
                    } catch (NoSuchMethodException e) {
                        System.out.println("Wrong Constructor");
                    } catch (Exception e) {
                        System.out.println("Wrong Statement");
                    }
                }

            } else if (pointPos >= 0) {
                String name = cmd.substring(0, pointPos);
                String method = cmd.substring(pointPos + 1, leftBracketPos);
                String arg1 = cmd.substring(leftBracketPos + 1, rightBracketPos);
                Class argType = null;
                if (leftBracketPos == rightBracketPos - 1) {
                    System.out.println("Wrong Method");
                    continue;
                }
                if (arg1.charAt(0) == '\"' && arg1.charAt(arg1.length() - 1) == '\"') {
                    arg1 = arg1.substring(1, arg1.length() - 1);
                    argType = String.class;
                } else {
                    argType = Student.class;
                }
//                System.out.println(arg1);
                Student stu = students.get(name);
                if (stu == null) {
                    System.out.println("Wrong Variable");
                    continue;
                }
//                System.out.println(method);
                try {
                    Method method1 = stu.getClass().getMethod(method, argType);
                    if (argType == String.class) {
                        method1.invoke(stu, arg1);
                    } else {
                        var stu2 = students.get(arg1);
                        method1.invoke(stu, stu2);
                    }
                } catch (NoSuchMethodException e) {
                    System.out.println("Wrong Method");
                } catch (InvocationTargetException e) {
                    System.out.println("Wrong Variable");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("Wrong Variable");
                }
            }
        }

    }
}
