//Written by Tony Shu(ID: 1356141) and Liuying Cui(ID: 1344370)

public class state
{
    //declare variables
    private int num;
    private String character;
    private int next1;
    private int next2;
    private boolean isAlter;
    private boolean visited;

    //constructor for a state
    public state(int _num,String _char,int _n1,int _n2)
    {
        num=_num;
        character=_char;
        next1=_n1;
        next2=_n2;
        isAlter=false;
        visited=false;
    }

    //returns the next1 of the current state
    public int getNext1()
    {
        return next1;
    }

    //returns the next2 of the current state
    public int getNext2()
    {
        return next2;
    }

    //set next1 to a specific value
    public void setNext1(int i)
    {
        next1=i;
    }

    //set next2 to a specific value
    public void setNext2(int i)
    {
        next2=i;
    }

    //get the state number of the current state
    public int getNum()
    {
        return num;
    }

    //get the character stored in the state
    public String getCharacter()
    {
        return character;
    }

    //set the character to a specific value
    public void setCharacter(String s)
    {
        character=s;
    }

    //print out info stored in our state
    public void print()
    {
        if(!isAlter)
        {
            System.out.println(num+" "+character+" "+next1+" "+next2);
        }
        else
        {
            System.out.println(num+" "+character+" "+next1+" "+next2+" ALTERNATION");
        }

    }

    public void setAlter(boolean b)
    {
        isAlter=b;
    }

    public boolean getAlter()
    {
        return isAlter;
    }

    public void setVisited(boolean b)
    {
        visited=b;
    }

    public boolean getVisted()
    {
        return visited;
    }


}
