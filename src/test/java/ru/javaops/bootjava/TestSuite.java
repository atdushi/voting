package ru.javaops.bootjava;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"ru.javaops.bootjava.voting.web", "ru.javaops.bootjava.user.web"})
public class TestSuite {
}
