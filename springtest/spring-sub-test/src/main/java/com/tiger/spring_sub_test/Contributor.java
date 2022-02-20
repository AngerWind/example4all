package com.tiger.spring_sub_test;

import lombok.Data;
import lombok.SneakyThrows;


import java.util.*;

/**
 * 关于{@link org.springframework.boot.context.config.ConfigDataEnvironmentContributor.ContributorIterator} 的测试
 */
@Data
public class Contributor implements Iterable<Contributor>{

    List<Contributor> children = new ArrayList<>();

    String name;
    boolean returnthis = true;

    @Override
    public String toString() {
        return name;
    }

    public Contributor(String name) {
        this.name = name;
    }

    @Override
    public Iterator<Contributor> iterator() {
        return new ContributorIterator(this.name);
    }

    public Contributor addChildren(Contributor contributor){
        this.children.add(contributor);
        return this;
    }

    private final class ContributorIterator implements Iterator<Contributor> {

        private Iterator<Contributor> children;

        private Iterator<Contributor> current;

        private Contributor next;

        private String name;

        private ContributorIterator(String name) {
            this.name = name;
            this.children = getChildren().iterator();
            this.current = Collections.emptyIterator();
        }

        @Override
        public boolean hasNext() {
            return fetchIfNecessary() != null;
        }

        @Override
        public Contributor next() {
            Contributor next = fetchIfNecessary();
            if (next == null) {
                throw new NoSuchElementException();
            }
            this.next = null;
            return next;
        }

        private Contributor fetchIfNecessary() {
            if (this.next != null) {
                return this.next;
            }
            if (this.current.hasNext()) {
                this.next = this.current.next();
                return this.next;
            }
            if (this.children.hasNext()) {
                this.current = this.children.next().iterator();
                return fetchIfNecessary();
            }
            if (returnthis) {
                returnthis = false;
                this.next = Contributor.this;
                return this.next;
            }
            return this.next;

        }
    }

    public static void main(String[] args) {
        Contributor root = new Contributor("00");
        Contributor tor1 = new Contributor("1");
        Contributor tor2 = new Contributor("2");
        Contributor tor3 = new Contributor("3");
        root.addChildren(tor1).addChildren(tor2).addChildren(tor3);

        Contributor tor11 = new Contributor("11");
        Contributor tor12 = new Contributor("12");
        tor1.addChildren(tor11).addChildren(tor12);

        Contributor tor111 = new Contributor("111");
        Contributor tor112 = new Contributor("112");
        tor11.addChildren(tor111).addChildren(tor112);

        Contributor tor21 = new Contributor("21");
        tor2.addChildren(tor21);


        Contributor tor31 = new Contributor("31");
        Contributor tor32 = new Contributor("32");
        tor3.addChildren(tor31).addChildren(tor32);

        Contributor tor321 = new Contributor("321");
        Contributor tor322 = new Contributor("322");
        tor32.addChildren(tor321).addChildren(tor322);

        for (Contributor contributor : root) {
            System.out.println(contributor);
        }
    }
}
