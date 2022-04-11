package Program;

public enum Entrance {
    Type1(100),
    Type2(100),
    Type3(100),
    Type4(100),
    Type5(100),
    Type6(100),
    Type7(100);


   private int entranceValue;


   Entrance(int value) {
        entranceValue = value;
    }

    public int getEntranceValue(){
       return entranceValue;
    }

}
