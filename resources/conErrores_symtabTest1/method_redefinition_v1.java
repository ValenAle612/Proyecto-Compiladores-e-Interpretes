///[Error:m1|13]

class C1{
    int m1(){}
}

class C2 extends C1{
    int m2(boolean b){}

}

class C3 extends C2{
    static int m1(){}

}

class A{
    static void main(){}
}