package Program;

public enum Exit { // TODO delete maybe?
    Type1(100),
    Type2(100),
    Type3(100),
    Type4(100),
    Type5(100),
    Type6(100),
    Type7(100);


    private int exitValue;


    Exit(int value) {
        exitValue = value;
    }

    public int getExitValue(){
        return exitValue;
    }

}

