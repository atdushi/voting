package com.github.atdushi;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"com.github.atdushi.voting.web", "com.github.atdushi.user.web"})
public class TestSuite {
}
