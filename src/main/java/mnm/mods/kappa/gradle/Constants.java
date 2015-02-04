package mnm.mods.kappa.gradle;

public interface Constants {

    public static final String CONFIG_COMPILE = "compile";
    public static final String CONFIG_APT_COMPILE = "aptCompile";

    public static final String ECLIPSE_FACTORYPATH_FILE = ".factorypath";
    public static final String ECLIPSE_FACTORYPATH = "factorypath";
    public static final String ECLIPSE_FACTORYPATH_ENTRY = "factorypathentry";

    public static final String ECLIPSE_APT_PREFS_FILE = ".settings/org.eclipse.jdt.apt.core.prefs";
    public static final String ECLIPSE_APT_PREFS_CONTENTS = ""
            + "eclipse.preferences.version=1\n"
            + "org.eclipse.jdt.apt.aptEnabled=true\n"
            + "org.eclipse.jdt.apt.genSrcDir=.apt_generated\n"
            + "org.eclipse.jdt.apt.reconcileEnabled=true";

    public static final String ECLIPSE_PROCESS_ANNOTATIONS = "org.eclipse.jdt.core.compiler.processAnnotations";

    public static final String PROCESSOR_FILE = "META-INF/services/javax.annotation.processing.Processor";
}
