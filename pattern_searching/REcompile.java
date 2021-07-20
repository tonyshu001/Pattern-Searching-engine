//Written by Tony Shu(ID: 1356141) and Liuying Cui(ID: 1344370)

import java.util.ArrayList;

public class REcompile
{
    public static String expression;
    public static int index;
    public static boolean valid=true;
    public static int state;
    public static ArrayList<state> stateList=new ArrayList<>();

    public static void main(String[] args)
    {
        //check if the user entered a regular expression
        if(args.length!=1)
        {
            System.err.println("invalid input");
            return;
        }

        //check if the regexp command-line argument encloses within double-quote characters
        expression=args[0];
        

        //build the start state
        state start=new state(state,"START",0,0);
        stateList.add(start);
        state++;


        try
        {
            //check if the expression is valid as well as starting building our FSM
            parse();
        }
        catch (Exception e)
        {
            error();
        }

        //if the expression is invalid then alert the user
        //otherwise output FSM
        if(valid)
        {

            for(int i=0;i<=stateList.size()-1;i++)
            {
                stateList.get(i).print();
            }
        }
        else
        {
            System.out.println(valid);
        }


    }

    //check if a character is a literal
    //return true if so, false otherwise
    public static boolean isVocab(char c)
    {
        int i=(int)c;
        return (i>=33&&i<=39)||(i>=44&&i<=45)||(i>=47&&i<=62)||(i>=64&i<=90)||(i>=94&&i<=123)||(i>=125&&i<=126);
    }

    //E -> T
    //E -> TE
    //E -> T|E
    //returns the start state of this expression
    public static int expression()
    {
        //get the start state of this term
        int r,t1,f1;
        t1=r=term();

        //if there is no more input, we are done
        if(index>expression.length()-1)
        {
            return r;
        }

        //if this is concatenation
        if(expression.charAt(index)=='.'||isVocab(expression.charAt(index))||expression.charAt(index)=='\\'
                ||expression.charAt(index)=='['||expression.charAt(index)=='(')
        {
            //get the start state of this expression and
            //update next1 and next2
            int t2,cur;
            cur=state-1;
            t2=expression();
            if(stateList.get(cur).getNext1()==stateList.get(cur).getNext2())
            {
                stateList.get(cur).setNext1(t2);
            }
            stateList.get(cur).setNext2(t2);

        }

        //if there is no more input, we are done
        if(index>=expression.length())
        {
            return r;
        }

        //if this is alternation
        if(expression.charAt(index)=='|')
        {
            //if there is nothing after '|', it is a invalid expression
            index++;
            if(index>expression.length()-1)
            {
                error();
            }
            else
            {
                //create a branch state,set the nex1 of the branch state to the term
                //so a|b and a|b|c both work
                int t2;
                f1=state-1;

                state st=new state(state,"BR",t1,-1);
                stateList.add(st);
                r=state;
                state++;

                //get the start state of this expression
                t2=expression();

                //update the branch state's next2 to the start state of the expression
                stateList.get(f1+1).setNext2(t2);

                //update term's next1 and next2
                if(stateList.get(f1).getNext1()==stateList.get(f1).getNext2())
                {
                    stateList.get(f1).setNext1(state);
                }
                stateList.get(f1).setNext2(state);

            }

        }


        return r;
    }

    //T -> F
    //T -> F*
    //T -> F+
    //T -> F?
    //returns the start state of this term
    public static int term()
    {
        //get the start state of this factor
        int r;
        int t1;
        t1=r=factor();

        //if there is no more input, we are done
        if(index>expression.length()-1)
        {
            return r;
        }

        //if it is closure
        if(expression.charAt(index)=='*')
        {
            int f=state-1;
            //create a branch state and set its next1 and next2
            state st=new state(state,"BR",t1,state+1);
            stateList.add(st);

            //check if the expression is something like (abc)*
            //if so, update the corresponding state so both a* and (abc)* work
            if(expression.charAt(index-1)!=')')
            {
                if(stateList.get(f-1).getNext1()==stateList.get(f-1).getNext2())
                {
                    stateList.get(f-1).setNext1(state);
                }
                stateList.get(f-1).setNext2(state);

            }

            index++;
            r=state;
            state++;

        }
        //preceding regexp can occur one or more times
        else if(expression.charAt(index)=='+')
        {
            int f=state-1;
            stateList.get(f).setNext1(t1);
            index++;

        }
        // preceding regexp can occur zero or one time
        else if(expression.charAt(index)=='?')
        {
            int f =state-1;
            //check if the expression is something like (abc)?
            //if so update corresponding state so both a? and (abc)? work
            if(expression.charAt(index-1)!=')')
            {
                //create a branch state and update the corresponding state
                state st=new state(state,"BR",t1,state+1);

                String previousChar=stateList.get(f).getCharacter();

                if(stateList.get(f).getAlter())
                {
                    stateList.get(f).setAlter(false);
                    st.setAlter(true);
                }

                stateList.get(f).setCharacter("BR");
                stateList.get(f).setNext2(state+1);
                st.setCharacter(previousChar);
                st.setNext1(state+1);
                stateList.add(st);

                r=f;
                state++;
                index++;
            }
            else
            {
                //create a branch state and update the corresponding state
                if(stateList.get(f).getNext1()==stateList.get(f).getNext2())
                {
                    stateList.get(f).setNext1(state+1);
                }
                stateList.get(f).setNext2(state+1);

                state st=new state(state,"BR",t1,state+1);
                stateList.add(st);

                r=state;
                state++;
                index++;
            }


        }


        return r;
    }

    //F -> .
    //F -> a (literal)
    //F -> \a
    //F -> [ALTERNATION]
    //F -> (E)
    //returns the start state of this factor
    public static int factor()
    {
        int r=0;

        if(index<=expression.length()-1)
        {
            //build FSM for a .
            if(expression.charAt(index)=='.')
            {
                String s=Character.toString('.');
                state st=new state(state,s,state+1,state+1);
                stateList.add(st);
                index++;
                r=state;
                state++;
            }
            //build FSM for a literal
            else if(isVocab(expression.charAt(index)))
            {
                String s=Character.toString(expression.charAt(index));
                state st=new state(state,s,state+1,state+1);
                stateList.add(st);
                index++;
                r=state;
                state++;
            }
            //build FSM for a escape character
            else if(expression.charAt(index)=='\\')
            {
                index++;
                String s=Character.toString(expression.charAt(index));
                state st=new state(state,s,state+1,state+1);
                stateList.add(st);
                r=state;
                state++;
                index++;
            }
            //build FSM for a square brackets
            //stores all symbols in the square brackets as a string
            else if(expression.charAt(index)=='[')
            {
                String s="";
                index++;
                //check if the first character is ']'
                //if so,add ']' to the string and keep looking for the closing bracket
                if(expression.charAt(index)==']')
                {
                    s+="]";
                    index++;
                }

                boolean foundEnd=false;
                //we need to ensure the square bracket has a closing bracket
                while(index<=expression.length()-1)
                {
                    if(expression.charAt(index)==']')
                    {
                        index++;
                        foundEnd=true;
                        break;
                    }
                    else
                    {
                        String cur=Character.toString(expression.charAt(index));
                        s+=cur;
                        index++;
                    }

                }

                //if it does not have a closing bracket, it is invalid expression
                if(!foundEnd)
                {
                    error();
                }
                else
                {
                    //otherwise build FSM for square bracket
                    state st=new state(state,s,state+1,state+1);
                    st.setAlter(true);
                    stateList.add(st);
                    r=state;
                    state++;
                }
            }
            //check if the facter is (E)
            //if we see a '(' we must then find an expression and a following ')' to ensure the expression is valid
            else if(expression.charAt(index)=='(')
            {
                index++;
                r=expression();
                if(expression.charAt(index)!=')')
                {
                    error();
                }
                else
                {
                    index++;

                }
            }
            else
            {
                error();
            }

        }
        return r;
    }

    //start point to build our FSM
    //set the start point and end point of our FSM
    public static void parse()
    {
        int initial;
        initial=expression();
        stateList.get(0).setNext1(initial);
        stateList.get(0).setNext2(initial);
        if(index<=expression.length()-1)
        {
            error();
        }
        else
        {
            state st=new state(state,"END",-1,-1);
            stateList.add(st);
        }
    }

    //this method acts like a trigger which tells the user the expression is invalid
    public static void error()
    {
        valid=false;

    }

}
