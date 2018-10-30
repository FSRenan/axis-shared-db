package FileManager;

import java.io.Serializable;

/**
 *
 * @author rferreira
 */
public class Person implements Serializable {

    private int age;
    private String cpf;
    private String name;

    public Person() {

    }

    public Person(int age, String cpf, String name) {
        this.age = age;
        this.cpf = cpf;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllInfo() {
        return "\nName: " + this.name + "\nAge: " + this.age + "\nCPF: " + this.cpf + "\n";
    }
}
