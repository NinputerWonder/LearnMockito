package org.wonder;

public class Person {
    private final String name;
    private final int age;
    private Salary salary;

    Person() {
        name = null;
        age = 0;
        salary = new Salary(0, "USD");
    }

    Person(String name, int age, Salary salary) {
        this.name = name;
        this.age = age;
        this.salary = salary == null ? new Salary(0, "USD") : salary;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getSalaryReport() {
        return salary.getAmount() + salary.getCurrency();
    }
}
