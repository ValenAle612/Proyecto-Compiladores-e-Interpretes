//a en bloque 0 &A&a en bloque 2 &A&x en bloque 2 &X&x en bloque 5 &3&a en bloque 5 &AAa&a en bloque 4 &AAa&a en bloque 3 &AAa&a en bloque 0 &AAa&exitosamente
class A {
    static void main() {
        var a = "A";
        System.printS("a en bloque 0 ");
        System.printSln(a); //A
        {
            {
                var x = "X";
                System.printS("a en bloque 2 ");
                System.printSln(a); //A
                System.printS("x en bloque 2 ");
                System.printSln(x); //X
            }
            {
                {
                    {   var x = 3;
                        System.printS("x en bloque 5 ");
                        System.printIln(x); //3
                        a = "AAa";
                        System.printS("a en bloque 5 ");
                        System.printSln(a); //AAa
                    }
                    System.printS("a en bloque 4 ");
                    System.printSln(a); //AAa
                }
                System.printS("a en bloque 3 ");
                System.printSln(a); //AAa
            }
        }
        System.printS("a en bloque 0 ");
        System.printSln(a); //AAa
    }
}