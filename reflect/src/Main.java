import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

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


/**
 * 描述参数的性质
 */
class Argu {
    Object arguVal;
    Class arguType;

    public Argu(String arguVal, Class arguType) {
        this.arguVal = arguVal;
        this.arguType = arguType;
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    public Argu(String str) {
        // TODO:只能解决参数类型为数字、student和字符串的情况
        if (str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') {
            this.arguVal = str.substring(1, str.length() - 1);
            this.arguType = String.class;
        } else if(isNumeric(str)){
            this.arguVal = Integer.parseInt(str);
            this.arguType = Integer.class;
        }else {
            this.arguVal = Main.students.get(str);
            this.arguType = Student.class;
        }
    }

    @Override
    public String toString() {
        return "Argu{" +
                "arguVal=" + arguVal +
                ", arguType=" + arguType.getSimpleName() +
                '}';
    }
}

public class Main {
    // 记录已经声明的变量
    static HashMap<String, Student> students = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            // 描述字符串的一些特征
            int equalPos = -1, pointPos = -1,
                    leftBracketPos = -1, rightBracketPos = -1,
                    blankPos = -1, newPos = -1;
            ArrayList<Integer> commaPoss = new ArrayList<>();

            //遍历数组获取特征
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
                    commaPoss.add(i);
                }
                if (i > 3) {
                    if (cmd.charAt(i) == ' ' && cmd.charAt(i - 1) == 'w' && cmd.charAt(i - 2) == 'e' && cmd.charAt(i - 3) == 'n') {
                        newPos = i;
                    }
                }
            }

            String cmdArgs = cmd.substring(leftBracketPos + 1, rightBracketPos);
            //重定位逗号的位置
            for (int i = 0; i < commaPoss.size(); i++) {
                commaPoss.set(i, commaPoss.get(i) - leftBracketPos - 1);
            }
            // 获取所有参数
            ArrayList<Argu> funArgs = new ArrayList<>();
            if (commaPoss.size() == 0) {
                if (leftBracketPos != rightBracketPos - 1) {
                    funArgs.add(new Argu(cmdArgs));
                }

            } else {
                for (int i = 0; i <= commaPoss.size(); i++) {
                    String temp;

                    if (i == 0) {
                        temp = cmdArgs.substring(0, commaPoss.get(i));
                    } else if (i == commaPoss.size()) {
                        temp = cmdArgs.substring(commaPoss.get(i - 1) + 1);
                    } else {
                        temp = cmdArgs.substring(commaPoss.get(i - 1) + 1, commaPoss.get(i));
                    }
                    funArgs.add(new Argu(temp));
                }
            }

            // 获取所有参数类型和值
            ArrayList<Class> argTypes = new ArrayList<>();

            for (int i = 0; i < funArgs.size(); i++) {
                argTypes.add(funArgs.get(i).arguType);
            }
            //将类型转换成数组
            Class[] classes = new Class[argTypes.size()];
            for (int i = 0; i < argTypes.size(); i++) {
                classes[i] = argTypes.get(i);
            }
            ArrayList<Object> argValues = new ArrayList<>();
            for (int i = 0; i < funArgs.size(); i++) {
                argValues.add(funArgs.get(i).arguVal);
            }
            // 将值转换成数组
            Object[] argVs = argValues.toArray();
            // 对于赋值语句的处理
            if (equalPos >= 0) {
                String typeName = cmd.substring(newPos + 1, leftBracketPos);
                String name;
                if (equalPos < blankPos) {
                    name = cmd.substring(0, equalPos);
                } else {
                    name = cmd.substring(blankPos + 1, equalPos);
                }

                try {
                    Object obj = Class.forName(typeName)
                            .getConstructor(classes)
                            .newInstance(argVs);
                    students.put(name, (Student) obj);
                } catch (ClassNotFoundException e) {
                    System.out.println("Wrong Type");
                } catch (NoSuchMethodException e) {
                    System.out.println("Wrong Constructor");
                } catch (Exception e) {
                    System.out.println("Wrong Statement");
                }

            }
            // 对于对象执行方法的处理
            else if (pointPos >= 0) {
                String name = cmd.substring(0, pointPos);
                Student student = students.get(name);
                String method = cmd.substring(pointPos + 1, leftBracketPos);

                try {
                    Method method1 = student.getClass().getMethod(method,classes);
                    method1.invoke(student,argVs);
                } catch (NoSuchMethodException e) {
                    System.out.println("Wrong Method");
                } catch (InvocationTargetException | NullPointerException e) {
                    System.out.println("Wrong Variable");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
