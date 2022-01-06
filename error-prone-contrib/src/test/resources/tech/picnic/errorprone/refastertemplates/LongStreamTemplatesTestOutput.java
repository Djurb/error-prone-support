package tech.picnic.errorprone.refastertemplates;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import java.util.OptionalLong;
import java.util.function.LongPredicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

final class LongStreamTemplatesTest implements RefasterTemplateTestCase {
  @Override
  public ImmutableSet<?> elidedTypesAndStaticImports() {
    return ImmutableSet.of(Streams.class);
  }

  LongStream testLongStreamClosedOpenRange() {
    return LongStream.range(0, 42);
  }

  LongStream testConcatOneLongStream() {
    return LongStream.of(1);
  }

  LongStream testConcatTwoLongStreams() {
    return LongStream.concat(LongStream.of(1), LongStream.of(2));
  }

  LongStream testFilterOuterLongStreamAfterFlatMap() {
    return LongStream.of(1).flatMap(v -> LongStream.of(v * v)).filter(n -> n > 1);
  }

  LongStream testFilterOuterStreamAfterFlatMapToLong() {
    return Stream.of(1).flatMapToLong(v -> LongStream.of(v * v)).filter(n -> n > 1);
  }

  LongStream testMapOuterLongStreamAfterFlatMap() {
    return LongStream.of(1).flatMap(v -> LongStream.of(v * v)).map(n -> n * 1);
  }

  LongStream testMapOuterStreamAfterFlatMapToLong() {
    return Stream.of(1).flatMapToLong(v -> LongStream.of(v * v)).map(n -> n * 1);
  }

  LongStream testFlatMapOuterLongStreamAfterFlatMap() {
    return LongStream.of(1).flatMap(v -> LongStream.of(v * v)).flatMap(LongStream::of);
  }

  LongStream testFlatMapOuterStreamAfterFlatMapToLong() {
    return Stream.of(1).flatMapToLong(v -> LongStream.of(v * v)).flatMap(LongStream::of);
  }

  ImmutableSet<Boolean> testLongStreamIsEmpty() {
    return ImmutableSet.of(
        LongStream.of(1).findAny().isEmpty(),
        LongStream.of(2).findAny().isEmpty(),
        LongStream.of(3).findAny().isEmpty(),
        LongStream.of(4).findAny().isEmpty());
  }

  ImmutableSet<Boolean> testLongStreamIsNotEmpty() {
    return ImmutableSet.of(
        LongStream.of(1).findAny().isPresent(),
        LongStream.of(2).findAny().isPresent(),
        LongStream.of(3).findAny().isPresent(),
        LongStream.of(4).findAny().isPresent());
  }

  OptionalLong testLongStreamMin() {
    return LongStream.of(1).min();
  }

  ImmutableSet<Boolean> testLongStreamNoneMatchLongPredicate() {
    LongPredicate pred = i -> i > 0;
    return ImmutableSet.of(
        LongStream.of(1).noneMatch(n -> n > 1),
        LongStream.of(2).noneMatch(pred),
        LongStream.of(3).noneMatch(pred));
  }

  boolean testLongStreamNoneMatch() {
    return LongStream.of(1).noneMatch(n -> n > 1);
  }

  ImmutableSet<Boolean> testLongStreamAnyMatch() {
    return ImmutableSet.of(
        LongStream.of(1).anyMatch(n -> n > 1), LongStream.of(2).anyMatch(n -> n > 2));
  }

  boolean testLongStreamAllMatchLongPredicate() {
    LongPredicate pred = i -> i > 0;
    return LongStream.of(1).allMatch(pred);
  }

  boolean testLongStreamAllMatch() {
    return LongStream.of(1).allMatch(n -> n > 1);
  }
}
