package com.journey.other.common;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.run;

import java.util.Random;

import org.junit.Test;

import io.vavr.Function2;
import io.vavr.Lazy;
import io.vavr.Patterns;
import io.vavr.Predicates;
import io.vavr.Tuple2;
import io.vavr.collection.CharSeq;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;

/**
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2020-11-12
 */
public class VavrTest {

    @Test
    public void matchApi() {
        int val = 3;
        String s = Match(val).of(
                Case($(1), "one"),
                Case($(Predicates.is(2)), "two"),
                Case($(Predicates.isIn(3, 4)), "three or four"),
                Case($(), "?")
        );
        Match(val).of(
                Case($(3), o -> run(() -> System.out.println("333333"))),
                Case($(), o -> run(() -> System.out.println("11111")))
        );
        Object obj = 1D;
        Number plusOne = Match(obj).of(
                Case($(Predicates.instanceOf(Integer.class)), i -> i + 1),
                Case($(Predicates.instanceOf(Double.class)), d -> d + 1.111),
                Case($(), o -> { throw new NumberFormatException(); })
        );
        System.out.println(plusOne.getClass() + ": " + plusOne.doubleValue());

        String rs = Match(Try.of(() -> "121")).of(
                Case(Patterns.$Success($("123")), value -> "good job"),
                Case(Patterns.$Success($()), value -> "default success"),
                Case(Patterns.$Failure($()), error -> "error")
        );
        System.out.println(rs);
    }


    @Test
    public void testTry() {
        Try<Void> voidTry = Try.run(() -> {int a = 1 / 0;});
        System.out.println(voidTry.isSuccess());
        voidTry.onFailure((c) -> System.out.println("fail" + c))
                .onSuccess((c) -> System.out.println("ok" + c));
        System.out.println(voidTry.getOrElseGet((e) -> null));
    }

    @Test
    public void collection() {
        //a,b,c
        System.out.println(List.of("a", "b", "c").mkString(","));
        System.out.println(List.of(1, 2, 3).sum());
        // List((a, 0), (b, 1), (c, 2))
        List<Tuple2<String, Integer>> list = List.of("a", "b", "c").zipWithIndex();
    }

    @Test
    public void function() {
        Function2<Integer, Integer, Integer> function2 = (a, b) -> (a + b) * 2;
        Function2<Integer, Integer, Option<Integer>> optionFunction2 = Function2.lift(Integer::sum);
    }

    @Test
    public void value() {
        //Lazy
        Lazy<Double> lazy = Lazy.of(Math::random);
        System.out.println(lazy.isEvaluated() + "[1]" + lazy.get());
        System.out.println(lazy.isEvaluated() + "[2]" + lazy.get());
        CharSequence chars = Lazy.val(() -> "Yay!", CharSequence.class);
        System.out.println(chars);
        //Validation
        Validation<Seq<String>, Person> valid = new PersonValidator().validatePerson("John Doe", -30);
        System.out.println("Validation person isInvalid:" + valid.isInvalid() + ", toString:" + valid.toString());
        //Either
        Either<Throwable, String> either =
                new Random().nextBoolean() ? Either.left(new RuntimeException()) : Either.right("good");
        System.out.println(either + "," + (either.isRight() ? either.get() : either.getLeft()));
    }

    private static class PersonValidator {
        private static final String VALID_NAME_CHARS = "[a-zA-Z]";

        public Validation<Seq<String>, Person> validatePerson(String name, int age) {
            return Validation.combine(validateName(name), validateAge(age)).ap(Person::new);
        }

        private Validation<String, String> validateName(String name) {
            return CharSeq.of(name)
                    .replaceAll(VALID_NAME_CHARS, "")
                    .transform(seq -> seq.isEmpty() ? Validation.valid(name) : Validation
                            .invalid("Name contains invalid " + "characters: '" + seq.distinct().sorted() + "'"));
        }

        private Validation<String, Integer> validateAge(int age) {
            return age < 0
                   ? Validation.invalid("Age must be at least 0")
                   : Validation.valid(age);
        }

    }

    private static class Person {
        public final String name;
        public final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person(" + name + ", " + age + ")";
        }
    }


}
