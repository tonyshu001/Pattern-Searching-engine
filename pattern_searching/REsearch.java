//Written by Tony Shu(ID: 1356141) and Liuying Cui(ID: 1344370)

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class REsearch
{
    public static ArrayList<state> dictionary=new ArrayList<>();

    public static void main(String[] args)
    {
        //check if the user has specified a file name to cope with
        if(args.length!=1)
        {
            System.err.println("please specify a file name");
            return;
        }

        try
        {
            //declare variables
            int mark=0;
            int point=0;
            dequeue deque=new dequeue();
            Scanner sc=new Scanner(System.in);


            //store all FSM from compiler into a dictionary
            //if the expression is invalid then alter the user
            while (sc.hasNextLine())
            {
                String data = sc.nextLine();
                String[] state=data.split(" ");

                if(state.length==1)
                {
                    System.err.println("invalid expression");
                    return;
                }

                state s=new state(Integer.parseInt(state[0]),state[1],Integer.parseInt(state[2]),Integer.parseInt(state[3]));
                if(state.length==5)
                {
                    s.setAlter(true);
                }

                dictionary.add(s);
            }
            sc.close();

            //read input from text file
            File f=new File(args[0]);
            Scanner myReader=new Scanner(f);

            //set the start point and end point of our FSM
            int start=dictionary.get(0).getNext1();
            int end = dictionary.get(dictionary.size()-1).getNum();

            //as long as we still have more input
            while(myReader.hasNextLine())
            {

                mark=0;
                point=0;
                //read the line
                String line=myReader.nextLine();
                setAllVisitedFalse();
                dictionary.get(0).setVisited(true);
                deque.restart();

                //push the start point into our dequeue
                deque.push(dictionary.get(start));
                boolean succeed=false;

                //as long as we have not reached the end of line
                while (point<=line.length()-1)
                {
                    //as long as the stack part of dequeue is not empty
                    while(!deque.stackEmpty())
                    {
                        //pop off the first element
                        state curr=deque.pop();
                        int num=curr.getNum();
                        dictionary.get(num).setVisited(true);

                        //if the current state is a branch state
                        if(curr.getCharacter().equals("BR"))
                        {
                        
                             //if the current state is the last state of FSM
                            //we found a match
                            if(curr.getNext1()==end||curr.getNext2()==end)
                            {
                                succeed=true;
                                System.out.println(line);
                                break;
                            }
                        
                            //push its next 1 and next 2 on to the stack
                            if(curr.getNext1()!=curr.getNext2())
                            {
                                if(!dictionary.get(curr.getNext2()).getVisted())
                                {
                                    deque.push(dictionary.get(curr.getNext2()));
                                }
                            }

                            if(!dictionary.get(curr.getNext1()).getVisted())
                            {
                                deque.push(dictionary.get(curr.getNext1()));
                            }


                        }
                        else
                        {
                            //otherwise we have to match something
                            if(point<=line.length()-1)
                            {
                                //if we match something
                                if(isMatched(line.charAt(point),curr))
                                {
                                
                                    //if the current state is the last state of FSM
                                    //we found a match
                                    if(curr.getNext1()==end||curr.getNext2()==end)
                                    {
                                        succeed=true;
                                        System.out.println(line);
                                        break;
                                    }
				        
                                    //put the next possible state onto the queue
                                    if(curr.getNext1()!=curr.getNext2())
                                    {
                                        deque.put(dictionary.get(curr.getNext2()));
                                    }

                                    deque.put(dictionary.get(curr.getNext1()));


                                }
                            }
                     
                        }
                    }

                    //if we found a match, we can break the loop
                    if(succeed)
                    {
                        break;
                    }

                    //we can swap the stack and queue and continue searching if queue is not empty
                    if(!deque.queueEmpty())
                    {
                        //advance the pointer
                        point++;
                        deque.swap();
                        setAllVisitedFalse();
                        dictionary.get(0).setVisited(true);
                    }
                    else
                    {
                        //if both stack and queue are empty, we failed to find a match
                        mark++;
                        point=mark;
                        setAllVisitedFalse();
                        dictionary.get(0).setVisited(true);
                        deque.push(dictionary.get(start));
                    }


                }

            }

        }
        catch (Exception e)
        {
            System.err.println(e);
        }


    }

    //this method sets all state stored in our dictionary to unvisited state
    public static void setAllVisitedFalse()
    {
        for(int i=0;i<dictionary.size();i++)
        {
            dictionary.get(i).setVisited(false);
        }
    }

    //returns true if the current character is matched
    //false otherwise
    public static boolean isMatched(char c,state s)
    {
        if(s.getCharacter().equals(".")&&!s.getAlter())
        {
            return true;
        }
        return s.getCharacter().contains(String.valueOf(c));
    }

}
