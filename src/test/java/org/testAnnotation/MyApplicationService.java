package org.testAnnotation;

public class MyApplicationService {
    private MyDomainService1 myDomainService1;
    private MyDomainService2 myDomainService2;

    public MyApplicationService(MyDomainService1 myDomainService1, MyDomainService2 myDomainService2) {
        this.myDomainService1 = myDomainService1;
        this.myDomainService2 = myDomainService2;
    }

    Integer cal(Integer count, Integer times) {
        return myDomainService1.increase(count) - myDomainService2.multi(times);
    }


}
