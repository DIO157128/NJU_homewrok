public class PolynList {

    //多项式相加
    public Node add(Node link1, Node link2) {
        Node p1=link1.next;
        Node p2=link2.next;
        Node res=new Node();
        Node p=res;
        if(p1==null&&p2==null){
            return null;
        }
        if(p1==null||p2==null){
            return p1==null?p2:p1;
        }
        else {
            while (p1!=null&&p2!=null){
                if(p1.exp==p2.exp){
                    p1.coef=p1.coef+p2.coef;
                    p2=p2.next;
                    if(p1.coef==0){
                        p1=p1.next;
                    }
                    else {
                        res.next=p1;
                        res=res.next;
                        p1=p1.next;
                    }
                }
                else if(p1.exp>p2.exp){
                    res.next=p2;
                    res=res.next;
                    p2=p2.next;
                }
                else {
                    res.next=p1;
                    res=res.next;
                    p1=p1.next;
                }
            }
            if(p1==null){
                res.next=p2;
            }
            else res.next=p1;
        }
        return p.next;
   }
   }
