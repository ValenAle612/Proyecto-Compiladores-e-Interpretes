///[Error:=|17]
// La variable v2 tiene tipo int por la dec anterior, no se le puede asignar un string
class A {
    public int a1;
    
     void m1(int p1)
    
    {
        var v1 = 4;
        {
            {
                var v2 = v1 * 3;
                {

                }
                {
                    v2 = "hola";
                }
            }
        }
       
    }
    

         
    

}



class Init{
    static void main()
    { }
}


