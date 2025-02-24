# Error Prone Workshop

The slides of the workshop: [EPS Workshop JFall][eps-workshop-jfall].

## Initial setup of the workshop

1. Start by forking the [`PicnicSupermarket/error-prone-support`][eps-github]
   repository on GitHub. Make sure to deselect _Copy the master branch only_.
2. Set the `workshop` branch as the `default` branch in the fork. Go to
   _Settings -> General -> Default Branch_ for this.
3. Go to the `Actions` tab of your repository and click the green _Enable_
   button to allow running of workflows.
4. Clone the repository locally.
5. Make sure to run `mvn clean install` in the root of this repository.
6. Open your code editor and familiarize yourself with the project structure.

Next to unit tests, the workshop comes with an integration testing framework
that relies on `grep` and `sed`. So if you are a macOS user, please run the
following commands:

* `brew install grep`
* `brew install gsed`

To verify that the test setup works, please run this command:

```sh
./integration-tests/checkstyle-10.12.4.sh
```

The script should end with the following output:

```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  01:49 min
[INFO] Finished at: 2023-10-31T19:56:46+01:00
[INFO] ------------------------------------------------------------------------
Inspecting changes...
Inspecting emitted warnings...
```

With IntelliJ IDEA, try to run the `WorkshopRefasterRulesTest` test. You might
see the following error:

```
java: exporting a package from system module jdk.compiler is not allowed with --release
```

If this happens, go to _Settings -> Build, Execution, Deployment -> Compiler ->
Java Compiler_ and deselect the option _Use '--release' option for
cross-compilation (Java 9 and later)_.

Now the project is ready for the rest of the workshop.

Important to note is that for every commit pushed and pull request opened, a
GitHub Action will be triggered to run the integration tests. In case of a
failure, there is the option to download artifacts that contain information on
the changes that are introduced by the new rules.

## Part 1: Writing Refaster rules

During this part of the workshop we will implement multiple Refaster rules.

Go to the `workshop` module and open the
`tech.picnic.errorprone.workshop.refasterrules` package. There you can find one
example and 5 different exercises to do. Make sure to check out the
`WorkshopRefasterRulesTest.java` class where you can enable tests. Per
assignment there is a test in this class that one can enable (by dropping the
`@Disabled` annotation) to validate your changes. The goal is to implement or
improve the Refaster rules such that the enabled tests pass.

Tips:

* The `XXX:` comments explains what needs to happen.
* See the test cases for each Refaster rule by looking for the name of the
  Refaster rule prefixed with `test`. For example, the
  `WorkshopAssignment0Rules.java` rule collection has a Refaster rule named
  `ExampleStringIsEmpty`. In the `WorkshopAssignment0RulesTestInput.java` and
  `WorkshopAssignment0RulesTestOutput.java` files there is a
  `testExampleStringIsEmpty` method that shows the input and output to test the
  Refaster rule.

### Validating changes with the integration tests

We created an integration testing framework that allows us to see the impact of
the rules that are created. This testing framework can be executed locally and
via GitHub Actions.

If you want to test this locally, run the following commands:

```sh
mvn clean install -DskipTests -Dverification.skip
./integration-tests/checkstyle-10.12.4.sh
```

Once the process is complete, and changes are introduced, the following output
will be printed:

```
There are unexpected changes.
Inspect the changes here: /tmp/tmp.Cmr423L1pA/checkstyle-10.12.4-diff-of-diffs-changes.patch
```

This file is be provided for your review using your preferred text editor.
Alternatively, you can also navigate to the repository by going to the
`./integration-tests/.repos/checkstyle` directory and executing `git log -p` to
view the commit history and associated changes.

This shows the impact of the rules that you wrote when they are applied to
Checkstyle!

The other option is to execute the integration test via GitHub Actions. You
only need to commit and push to the branch. This will trigger execution of the
integration tests, which will run for about 10 minutes. When the build is
finished, go to the _Actions_ tab in your fork and navigate to your most recent
commit and click on it. Then click on _Summary_ and download the artifact
`integration-test-checkstyle-10.12.4` at the bottom. Once done, unzip the
artifact and inspect the `checkstyle-10.12.4-diff-of-diffs-changes.patch` file
to see the changes.

## Part 2: Writing Error Prone checks

During this part of the workshop we will implement parts of multiple Error
Prone `BugChecker`s. Each of these classes contain `XXX` comments explaining
what needs to be implemented. However, before diving in, make sure to first
navigate to a check's associated test class to drop the class-level `@Disabled`
annotation. Upon initial execution the tests will fail; the goal is to get then
to pass.

It is recommended (but not required) to solve the assignments in the following
order:

* `AssertJIsNullMethod.java`
* `DropMockitoEq.java`
* `JUnitTestMethodDeclaration.java`
* `DropAutowiredConstructorAnnotation.java`
* `DeleteIdentityConversion.java`

Some utility classes that you can use:

* `com.google.errorprone.util.ASTHelpers`: contains many common operations on
  the Abstract Syntax Tree.
* `com.google.errorprone.fixes.SuggestedFixes`: contains helper methods for
  creating `Fix`es.

To see the impact of the newly introduced Error Prone checks [re-trigger the
integration test framework](#validating-changes-with-the-integration-tests).

[eps-github]: https://github.com/PicnicSupermarket/error-prone-support
[eps-workshop-jfall]: https://drive.google.com/file/d/1Es1OuSUmPHSt3BjeCWfrXfoF-cJkkA1A/view
