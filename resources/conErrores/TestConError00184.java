///[Error:p1|14]
// Nombre de variable local o parametro repetido x - ln: 14
class A {
    public int a1;
    
     void m1(int p1)
    {
         var x = 1; 
       
        {
            {
                var y = 2;
            }
            {{{var p1 = 3;}}}

           {
                 var x = true;
           }
           
           
        }
        
       
    }
    
 
  
}




class Init{
    static void main()
    { }
}


