package tech.picnic.errorprone.bugpatterns.testngtojunit;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import java.util.Optional;

/**
 * POJO containing data collected using {@link TestNGScanner} for use in {@link
 * TestNGJUnitMigration}.
 */
@AutoValue
public abstract class TestNGMetadata {
  abstract ClassTree getClassTree();

  abstract AnnotationMetadata getClassLevelAnnotationMetadata();

  abstract ImmutableMap<MethodTree, AnnotationMetadata> getMethodAnnotations();

  /**
   * Get the {@link org.testng.annotations.DataProvider}s that are able to migratable.
   *
   * @return an {@link ImmutableMap} mapping the name of the data provider to it's respective
   *     metadata.
   */
  public abstract ImmutableMap<String, DataProviderMetadata> getDataProviderMetadata();

  static Builder builder() {
    return new AutoValue_TestNGMetadata.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    private final ImmutableMap.Builder<MethodTree, AnnotationMetadata> methodAnnotationsBuilder =
        new ImmutableMap.Builder<>();

    private final ImmutableMap.Builder<String, DataProviderMetadata> dataProviderMetadataBuilder =
        new ImmutableMap.Builder<>();

    abstract Builder setClassTree(ClassTree value);

    abstract AnnotationMetadata getClassLevelAnnotationMetadata();

    Optional<AnnotationMetadata> getOptClassLevelAnnotationMetadata() {
      try {
        return Optional.of(getClassLevelAnnotationMetadata());
      } catch (IllegalStateException ignored) {
        return Optional.empty();
      }
    }

    abstract Builder setClassLevelAnnotationMetadata(AnnotationMetadata value);

    abstract Builder setMethodAnnotations(ImmutableMap<MethodTree, AnnotationMetadata> value);

    ImmutableMap.Builder<MethodTree, AnnotationMetadata> methodAnnotationsBuilder() {
      return methodAnnotationsBuilder;
    }

    abstract Builder setDataProviderMetadata(ImmutableMap<String, DataProviderMetadata> value);

    ImmutableMap.Builder<String, DataProviderMetadata> dataProviderMetadataBuilder() {
      return dataProviderMetadataBuilder;
    }

    abstract TestNGMetadata autoBuild();

    TestNGMetadata build() {
      setMethodAnnotations(methodAnnotationsBuilder.build());
      setDataProviderMetadata(dataProviderMetadataBuilder.build());
      return autoBuild();
    }
  }

  ImmutableSet<AnnotationMetadata> getAnnotations() {
    return ImmutableSet.copyOf(getMethodAnnotations().values());
  }

  Optional<AnnotationMetadata> getAnnotation(MethodTree methodTree) {
    return Optional.ofNullable(getMethodAnnotations().get(methodTree));
  }

  /**
   * POJO containing data for a specific {@link org.testng.annotations.Test} annotation for use in
   * {@link TestNGJUnitMigration}.
   */
  @AutoValue
  public abstract static class AnnotationMetadata {
    abstract AnnotationTree getAnnotationTree();

    /**
     * A mapping for all arguments in the annotation to their value.
     *
     * @return an {@link ImmutableMap} mapping each annotation argument to their respective value.
     */
    public abstract ImmutableMap<String, ExpressionTree> getArguments();

    /**
     * Instantiate a new {@link AnnotationMetadata}.
     *
     * @param annotationTree the annotation tree
     * @param arguments the arguments in that annotation tree
     * @return the new {@link AnnotationMetadata} instance
     */
    public static AnnotationMetadata create(
        AnnotationTree annotationTree, ImmutableMap<String, ExpressionTree> arguments) {
      return new AutoValue_TestNGMetadata_AnnotationMetadata(annotationTree, arguments);
    }
  }

  /**
   * POJO containing data for a specific {@link org.testng.annotations.DataProvider} annotation for
   * use in {@link TestNGJUnitMigration}.
   */
  @AutoValue
  public abstract static class DataProviderMetadata {

    abstract MethodTree getMethodTree();

    abstract String getName();

    /**
     * Instantiate a new {@link DataProviderMetadata} instance.
     *
     * @param methodTree the value factory method tree
     * @return a new {@link DataProviderMetadata} instance
     */
    public static DataProviderMetadata create(MethodTree methodTree) {
      return new AutoValue_TestNGMetadata_DataProviderMetadata(
          methodTree, methodTree.getName().toString());
    }
  }
}
