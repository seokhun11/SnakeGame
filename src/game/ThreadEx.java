package game;

public class ThreadEx extends Thread{
    public static void main(String[] args){
        ThreadEx th = new ThreadEx();
        th.start();
    }

    public void run(){
        try{
            sleep(5000);
            return;
        }catch(InterruptedException e){
            return;}
    }
}