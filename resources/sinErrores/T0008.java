//1&&10&&15&exitosamente

class A extends System {
    static void main(){
        var a = 1;
        var b = a;//1
        var c = b;//1
        met1(b, 15);//1, 15
    }

    static void met1(int p1, int p2){
        var a = 10;
        debugPrint(p1);//1
        println();
        debugPrint(a);//10
        println();
        debugPrint(p2);//15
    }
}
