package org.injection;

public class Employee {
    private final Integer id;
    private final String name;
    private final Boolean active;

    Employee(Integer id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return active;
    }
}
