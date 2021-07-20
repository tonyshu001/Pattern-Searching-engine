//Written by Tony Shu(ID: 1356141) and Liuying Cui(ID: 1344370)

import java.util.Stack;

public class dequeue
{
    //our dequeue consists of two stacks
    private Stack<state> stack;
    private Stack<state> queue;

    //constructor for a dequeue, two stacks are both empty
    public dequeue()
    {
        stack=new Stack<>();
        queue=new Stack<>();
    }

    //pop off the first state stored in the stack
    public state pop()
    {
        return stack.pop();
    }

    //push a new state into the stack
    public void push(state s)
    {
        stack.push(s);
    }

    //check if the stack is empty
    //returns true if so
    //false otherwise
    public boolean stackEmpty()
    {
        return stack.isEmpty();
    }

    //check if the queue is empty
    //returns true if so
    //false otherwise
    public boolean queueEmpty()
    {
        return queue.isEmpty();
    }

    //push a new state into our queue
    public void put(state s)
    {
        queue.push(s);
    }

    //when the stack is empty while queue is not
    //swap them so the search can continue
    public void swap()
    {
        stack=queue;
        queue=new Stack<>();
    }

    //reset both stacks to empty
    //this method is typically used when ready to read next line
    public void restart()
    {
        stack=new Stack<>();
        queue=new Stack<>();
    }

    //returns the first element stored in the stack without changing anything
    public state peek()
    {
        return stack.peek();
    }
}
