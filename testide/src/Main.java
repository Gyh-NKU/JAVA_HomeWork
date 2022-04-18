import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
//┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
//
//│   (\_(\     (\_/)     (\_/)     (\_/)      (\(\   │
//    ( -.-)    (•.•)     (>.<)     (^.^)     (='.')
//│  C(")_(")  (")_(")   (")_(")   (")_(")   O(_")")  │
//
//└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘

class Student {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }
}


public class Main {

    public static void main(String[] args){

    }
}


