package tech.picnic.errorprone.bugpatterns;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.errorprone.matchers.Matchers.anyOf;
import static com.google.errorprone.matchers.method.MethodMatchers.instanceMethod;
import static com.google.errorprone.matchers.method.MethodMatchers.staticMethod;

import com.google.common.base.Splitter;
import com.google.errorprone.matchers.Matcher;
import com.sun.source.tree.ExpressionTree;
import java.util.Collection;
import java.util.regex.Pattern;

/** A method invocation expression {@link Matcher} factory. */
// XXX: Document better. The expressions accepted here could also be defined using `MethodMatchers`.
// So explain why this class is still useful.
final class MethodMatcherFactory {
  private static final Splitter ARGUMENT_TYPE_SPLITTER =
      Splitter.on(',').trimResults().omitEmptyStrings();
  private static final Pattern METHOD_SIGNATURE =
      Pattern.compile("([^\\s#(,)]+)#([^\\s#(,)]+)\\(((?:[^\\s#(,)]+(?:,[^\\s#(,)]+)*)?)\\)");

  Matcher<ExpressionTree> create(Collection<String> signatures) {
    return anyOf(
        signatures.stream()
            .map(MethodMatcherFactory::createMethodMatcher)
            .collect(toImmutableSet()));
  }

  // XXX: It seems parse errors are silently swallowed. Double-check; if true, file a ticket.
  // XXX: This (probably) doesn't work for methods with array type arguments; if true, implement a
  // fix.
  // XXX: The `nullness` warning suppression shouldn't be necessary. See
  // https://github.com/typetools/checker-framework/issues/4006.
  @SuppressWarnings("nullness:argument.type.incompatible")
  private static Matcher<ExpressionTree> createMethodMatcher(CharSequence signature) {
    java.util.regex.Matcher m = METHOD_SIGNATURE.matcher(signature);
    checkArgument(m.matches(), "Not a valid method signature: %s", signature);
    String className = m.group(1);
    String methodName = m.group(2);
    Iterable<String> parameterTypes = ARGUMENT_TYPE_SPLITTER.split(m.group(3));

    return anyOf(
        instanceMethod().onDescendantOf(className).named(methodName).withParameters(parameterTypes),
        staticMethod().onClass(className).named(methodName).withParameters(parameterTypes));
  }
}
