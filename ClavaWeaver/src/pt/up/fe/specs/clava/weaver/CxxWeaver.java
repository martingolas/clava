package pt.up.fe.specs.clava.weaver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.lara.interpreter.joptions.config.interpreter.LaraiKeys;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.language.specification.LanguageSpecification;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.antarex.clava.AntarexClavaLaraApis;
import pt.up.fe.specs.antarex.clava.JsAntarexApiResource;
import pt.up.fe.specs.clang.ClangAstParser;
import pt.up.fe.specs.clang.SupportedPlatform;
import pt.up.fe.specs.clang.ast.genericnode.ClangRootNode;
import pt.up.fe.specs.clang.clavaparser.ClavaParser;
import pt.up.fe.specs.clava.ClavaLog;
import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaOptions;
import pt.up.fe.specs.clava.Include;
import pt.up.fe.specs.clava.ast.extra.App;
import pt.up.fe.specs.clava.language.Standard;
import pt.up.fe.specs.clava.utils.SourceType;
import pt.up.fe.specs.clava.weaver.abstracts.weaver.ACxxWeaver;
import pt.up.fe.specs.clava.weaver.gears.InsideApplyGear;
import pt.up.fe.specs.clava.weaver.gears.ModifiedFilesGear;
import pt.up.fe.specs.clava.weaver.importable.AstFactory;
import pt.up.fe.specs.clava.weaver.importable.ClavaPlatforms;
import pt.up.fe.specs.clava.weaver.importable.Format;
import pt.up.fe.specs.clava.weaver.importable.LowLevelApi;
import pt.up.fe.specs.clava.weaver.joinpoints.CxxProgram;
import pt.up.fe.specs.clava.weaver.options.CxxWeaverOption;
import pt.up.fe.specs.clava.weaver.options.CxxWeaverOptions;
import pt.up.fe.specs.clava.weaver.pragmas.ClavaPragmas;
import pt.up.fe.specs.lang.SpecsPlatforms;
import pt.up.fe.specs.lara.LaraExtraApis;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.collections.AccumulatorMap;
import pt.up.fe.specs.util.csv.CsvField;
import pt.up.fe.specs.util.csv.CsvWriter;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.parsing.arguments.ArgumentsParser;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.ProgressCounter;
import pt.up.fe.specs.util.utilities.StringLines;

/**
 * Weaver Implementation for CxxWeaver<br>
 * Since the generated abstract classes are always overwritten, their implementation should be done by extending those
 * abstract classes with user-defined classes.<br>
 * The abstract class {@link pt.up.fe.specs.clava.weaver.abstracts.ACxxWeaverJoinPoint} can be used to add user-defined
 * methods and fields which the user intends to add for all join points and are not intended to be used in LARA aspects.
 *
 * @author Lara Weaver Generator
 */
public class CxxWeaver extends ACxxWeaver {

    static {
        // DevUtils.addDevProject(
        // new File("C:\\Users\\JoaoBispo\\Desktop\\shared\\specs-lara\\2017 COMLAN\\RangeValueMonitor\\lara"),
        // "COMLAN",
        // true, true);
    }

    public static LanguageSpecification buildLanguageSpecification() {
        return LanguageSpecification.newInstance(ClavaWeaverResource.JOINPOINTS, ClavaWeaverResource.ARTIFACTS,
                ClavaWeaverResource.ACTIONS, true);
    }

    private static final boolean SHOW_MEMORY_USAGE = true;

    private static final String TEMP_WEAVING_FOLDER = "__clava_woven";
    private static final String TEMP_SRC_FOLDER = "__clava_src";
    private static final String WOVEN_CODE_FOLDERNAME = "woven_code";

    private static final Set<String> LANGUAGES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("c", "cxx")));

    // private static final Set<String> EXTENSIONS_IMPLEMENTATION = new HashSet<>(Arrays.asList(
    // "c", "cpp"));
    // private static final Set<String> EXTENSIONS_HEADERS = new HashSet<>(Arrays.asList("h", "hpp"));

    public static String getWovenCodeFoldername() {
        return WOVEN_CODE_FOLDERNAME;
    }

    private static final List<String> DEFAULT_COMMON_DUMPER_FLAGS = Arrays.asList("-Wno-unknown-pragmas");
    // private static final List<String> DEFAULT_DUMPER_FLAGS = Arrays.asList();

    private static final List<String> DEFAULT_DUMPER_FLAGS = buildDefaultDumperFlags();

    private static List<String> buildDefaultDumperFlags() {
        List<String> defaultFlags = new ArrayList<>();

        // Default flags that are always added
        defaultFlags.addAll(DEFAULT_COMMON_DUMPER_FLAGS);

        SupportedPlatform currentPlatform = SupportedPlatform.getCurrentPlatform();

        // If MacOS, always add -ffreestanding
        if (currentPlatform == SupportedPlatform.MAC_OS) {
            defaultFlags.add("-ffreestanding");
        }

        return defaultFlags;
    }

    public static List<String> getDefaultFlags() {
        return DEFAULT_DUMPER_FLAGS;
    }

    private static final List<ResourceProvider> CLAVA_LARA_API = new ArrayList<>();
    static {
        CLAVA_LARA_API.addAll(LaraExtraApis.getApis());
        CLAVA_LARA_API.addAll(ClavaLaraApis.getApis());
        CLAVA_LARA_API.addAll(AntarexClavaLaraApis.getApis());
    }

    private static final List<Class<?>> CLAVA_IMPORTABLE_CLASSES = new ArrayList<>();
    static {
        CLAVA_IMPORTABLE_CLASSES.addAll(LaraExtraApis.getImportableClasses());
        CLAVA_IMPORTABLE_CLASSES.addAll(ClavaLaraApis.getImportableClasses());
        CLAVA_IMPORTABLE_CLASSES.addAll(
                Arrays.asList(SpecsPlatforms.class, AstFactory.class, Format.class, LowLevelApi.class, CsvWriter.class,
                        CsvField.class, ProgressCounter.class, ClavaPlatforms.class, ClavaWeaverLauncher.class,
                        ArgumentsParser.class, CxxWeaverApi.class));

    }

    /**
     * All definitions, including default LaraI keys and Clava-specific keys.
     */
    private static final Lazy<StoreDefinition> WEAVER_DEFINITION = Lazy.newInstance(() -> {
        return new StoreDefinitionBuilder("Clava Weaver")
                .addDefinition(LaraiKeys.STORE_DEFINITION)
                .addDefinition(CxxWeaverOption.STORE_DEFINITION)
                .build();
    });

    public static StoreDefinition getWeaverDefinition() {
        return WEAVER_DEFINITION.get();
    }

    // Weaver configuration
    private DataStore args = null;

    // Gears
    private final ModifiedFilesGear modifiedFilesGear;
    private final InsideApplyGear insideApplyGear;

    // Parsed program state
    // private Deque<App> apps;
    // private Deque<Map<ClavaNode, Map<String, Object>>> userValuesStack;

    // private File outputDir = null;
    // private List<File> originalSources = null;
    private List<File> currentSources = null;
    private List<String> userFlags = null;
    // private CxxJoinpoints jpFactory = null;

    // private File baseFolder = null;
    private List<String> parserOptions = new ArrayList<>();

    private Logger infoLogger = null;
    private Level previousLevel = null;

    private Set<String> messagesToUser;

    private AccumulatorMap<String> accMap;

    private ClavaWeaverData weaverData;

    public CxxWeaver() {
        // Gears
        this.modifiedFilesGear = new ModifiedFilesGear();
        this.insideApplyGear = new InsideApplyGear();

        // Weaver configuration
        args = null;

        // outputDir = null;
        currentSources = null;
        userFlags = null;

        // baseFolder = null;
        parserOptions = new ArrayList<>();

        infoLogger = null;
        previousLevel = null;

        // Set, in order to filter repeated messages
        // Linked, to preserve order
        messagesToUser = null;

        accMap = null;

        weaverData = null;

        this.setWeaverProfiler(new ClavaMetrics());
    }

    public App getApp() {
        return getAppTry().get();
    }

    public Optional<App> getAppTry() {
        if (weaverData == null) {
            return Optional.empty();
        }

        return weaverData.getAst();
    }

    // public File getBaseSourceFolder() {
    // return baseFolder;
    // }

    public CxxProgram getAppJp() {
        return CxxJoinpoints.programFactory(getApp());
    }

    private Map<ClavaNode, Map<String, Object>> getUserValues() {
        return weaverData.getUserValues();
        // return userValuesStack.peek();
    }

    public boolean addMessageToUser(String message) {
        return messagesToUser.add(message);
    }

    /**
     * Warns the lara interpreter if the weaver accepts a folder as the application or only one file at a time.
     *
     * @return true if the weaver is able to work with several files, false if only works with one file
     */
    @Override
    public boolean handlesApplicationFolder() {
        // Can the weaver handle an application folder?
        return true;
    }

    /**
     * Set a file/folder in the weaver if it is valid file/folder type for the weaver.
     *
     * @param source
     *            the file with the source code
     * @param outputDir
     *            output directory for the generated file(s)
     * @param args
     *            arguments to start the weaver
     * @return true if the file type is valid
     */
    @Override
    public boolean begin(List<File> sources, File outputDir, DataStore args) {
        reset();

        weaverData = new ClavaWeaverData(args);

        accMap = new AccumulatorMap<>();

        // Logger.getLogger(LoggingUtils.INFO_TAG).setLevel(Level.WARNING);
        //
        // Logger.getLogger(LoggingUtils.INFO_TAG).setUseParentHandlers(false);

        // TODO: Temporary solution, while we do not use SpecsLoggers
        if (args.get(CxxWeaverOption.DISABLE_CLAVA_INFO)) {
            // Needs to keep a strong reference, or it can be garbage collected
            infoLogger = Logger.getLogger(SpecsLogs.INFO_TAG);
            previousLevel = infoLogger.getLevel();
            infoLogger.setLevel(Level.WARNING);
        }

        // boolean disableWeaving = args.get(CxxWeaverOption.DISABLE_WEAVING);

        // Set weaver in CxxFactory, so that it can have access to a weaver configuration
        // This setting is local to the thread

        // Weaver arguments
        // this.originalSources = sources;
        this.currentSources = sources;
        // this.outputDir = outputDir;
        this.args = args;

        // Initialize list of options for parser
        parserOptions = new ArrayList<>();

        // Add all source folders as include folders
        Set<String> sourceIncludeFolders = getIncludeFlags(sources);

        parserOptions.addAll(sourceIncludeFolders);

        // Add normal include folders
        for (File includeFolder : args.get(CxxWeaverOption.HEADER_INCLUDES)) {
            parserOptions.add("-I" + parseIncludePath(includeFolder));
        }

        // Add system include folders
        for (File includeFolder : args.get(CxxWeaverOption.SYSTEM_INCLUDES)) {
            parserOptions.add("-isystem");
            parserOptions.add(parseIncludePath(includeFolder));
        }

        // Add standard
        parserOptions.add(getStdFlag());

        // Add default flags
        parserOptions.addAll(DEFAULT_DUMPER_FLAGS);

        // Add user flags
        userFlags = extractUserFlags(args);

        parserOptions.addAll(userFlags);

        // Initialize weaver with the input file/folder

        // First folder is considered the base folder
        // Only needs folder if we are doing weaving
        // if (!disableWeaving) {
        // baseFolder = getFirstSourceFolder(sources);
        // }

        // Init messages to user
        messagesToUser = new LinkedHashSet<>();

        // If weaving disabled, return now
        if (args.get(CxxWeaverOption.DISABLE_WEAVING)) {
            SpecsLogs.msgInfo("Weaving disabled, ignoring source-code files");
            // app = null;
            return true;
        }

        weaverData.pushAst(createApp(sources, parserOptions));

        // TODO: Option to dump clang and clava
        SpecsIo.write(new File("clavaDump.txt"), getApp().toString());

        return true;
    }

    private void reset() {
        // Reset gears
        modifiedFilesGear.reset();
    }

    public List<String> getUserFlags() {
        return userFlags;
    }

    private List<String> extractUserFlags(DataStore args) {
        String flagsString = args.get(ClavaOptions.FLAGS);
        List<String> flags = Arrays.stream(flagsString.split(" "))
                // Only consider non-empty strings
                .filter(string -> !string.isEmpty())
                .collect(Collectors.toList());

        return flags;
    }

    public String getStdFlag() {
        Standard standard = args.get(ClavaOptions.STANDARD);
        return "-std=" + standard.getString();
    }

    public Set<String> getIncludeFlags() {
        if (currentSources == null) {
            SpecsLogs.msgWarn("Source folders are not set");
            return Collections.emptySet();
        }

        return getIncludeFlags(currentSources);
    }

    private Set<String> getIncludeFlags(List<File> sources) {
        Set<String> sourceIncludeFolders = sources.stream()
                .map(source -> "-I" + parseIncludePath(source))
                // Collect to a set, to remove duplicates
                .collect(Collectors.toSet());
        return sourceIncludeFolders;
    }

    public Set<String> getIncludeFolders() {
        if (currentSources == null) {
            SpecsLogs.msgWarn("Source folders are not set");
            return Collections.emptySet();
        }

        return getIncludeFolders(currentSources);
    }

    private Set<String> getIncludeFolders(List<File> sources) {
        Set<String> sourceIncludeFolders = sources.stream()
                .map(source -> parseIncludePath(source))
                // Collect to a set, to remove duplicates
                .collect(Collectors.toSet());
        return sourceIncludeFolders;
    }

    private static File getFirstSourceFolder(List<File> sources) {
        Preconditions.checkArgument(!sources.isEmpty(), "Needs at least one source specified (file or folder)");

        File firstSource = sources.stream()
                .filter(source -> source.exists())
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Needs to specify at least one source (file or folder) that exists, found none. Input sources:"
                                + sources));

        if (firstSource.isDirectory()) {
            return firstSource;
        }

        if (firstSource.isFile()) {
            return firstSource.getParentFile();
        }

        throw new RuntimeException("Could not process source '" + firstSource + "'");
    }

    private static String parseIncludePath(File path) {
        // If file, get parent folder
        File folderPath = path.isFile() ? path.getParentFile() : path;

        String folderAbsPath = folderPath.getAbsolutePath();

        if (!folderAbsPath.contains(" ")) {
            return folderAbsPath;
        }

        // If on Windows, do not escape
        // if (Platforms.isWindows()) {
        // return folderAbsPath;
        // }

        return "\"" + folderAbsPath + "\"";
    }

    public App createApp(List<File> sources, List<String> parserOptions) {

        // All files, header and implementation
        Map<String, File> allFiles = SpecsIo.getFileMap(sources, SourceType.getPermittedExtensions());

        // List<String> implementationFilenames = processSources(sources);
        List<String> implementationFilenames = processSources(allFiles);

        // TODO: If option to separe include folders in generation is on, it should return just that folder
        // List<File> includeFolders = sources;

        // addFlagsFromFiles(includeFolders, filenames, parserOptions);
        addFlagsFromFiles(implementationFilenames, parserOptions);

        // Sort filenames so that select order of files is consistent between OSes
        Collections.sort(implementationFilenames);

        boolean useCustomResources = getConfig().get(ClavaOptions.CUSTOM_RESOURCES);

        // TODO: parse should receive File instead of String?
        long tic = System.nanoTime();
        ClangRootNode ast = new ClangAstParser(false, useCustomResources).parse(implementationFilenames, parserOptions);

        SpecsLogs.msgInfo(SpecsStrings.takeTime("Clang Parsing and Dump", tic));
        if (SHOW_MEMORY_USAGE) {
            SpecsLogs
                    .msgInfo("Current memory used (Java):" + SpecsStrings.parseSize(SpecsSystem.getUsedMemory(true)));
        }

        try (ClavaParser clavaParser = new ClavaParser(ast)) {
            tic = System.nanoTime();
            App app = clavaParser.parse();

            // Set source paths of each TranslationUnit
            app.setSources(allFiles);
            // app.setSources(sources);
            SpecsLogs.msgInfo(SpecsStrings.takeTime("Clang AST to Clava", tic));

            tic = System.nanoTime();
            ClavaPragmas.processClavaPragmas(app);
            SpecsLogs.msgInfo(SpecsStrings.takeTime("Weaver AST processing", tic));

            if (SHOW_MEMORY_USAGE) {
                SpecsLogs.msgInfo("Current memory used (Java):"
                        + SpecsStrings.parseSize(SpecsSystem.getUsedMemory(true)));
                // LoggingUtils.msgInfo("Heap size (Java):" + ParseUtils.parseSize(Runtime.getRuntime().maxMemory()));
            }

            return app;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // private void addFlagsFromFiles(List<File> includeFolders, List<String> filenames, List<String> parserOptions) {
    private void addFlagsFromFiles(List<String> filenames, List<String> parserOptions) {
        // If all files are .cl files, add flags -x cl
        long numberOfOpenCLFiles = filenames.stream()
                .filter(filename -> filename.endsWith(".cl"))
                .count();

        if (numberOfOpenCLFiles == filenames.size()) {
            parserOptions.add("-x");
            parserOptions.add("cl");
        }

        // // Adds sources as include folders
        // for (File includeFolder : includeFolders) {
        // // parserOptions.add("-I\"" + source.getAbsolutePath() + "\"");
        // parserOptions.add("\"-I" + includeFolder.getAbsolutePath() + "\"");
        // }

    }

    /*
    private List<String> processSources(List<File> sources) {
        // private Map<String, File> processSources(List<File> sources) {
        List<String> sourceFiles = SpecsIo.getFiles(sources, SourceType.IMPLEMENTATION.getExtensions());
        // Map<String, File> sourceFiles = SpecsIo.getFileMap(sources, SourceType.IMPLEMENTATION.getExtensions());
    
        if (!sourceFiles.isEmpty()) {
            return sourceFiles;
        }
    
        // No implementation files found. Create temporary file with only the includes
    
        StringBuilder code = new StringBuilder();
        for (File sourceFolder : sources) {
            // List<File> headerFiles = SpecsIo.getFilesRecursive(sourceFolder, App.getExtensionsHeaders());
            List<File> headerFiles = SpecsIo.getFilesRecursive(sourceFolder, SourceType.HEADER.getExtensions());
    
            // Create source code to call header
            String includeCode = headerFiles.stream()
                    .map(file -> SpecsIo.getRelativePath(file, sourceFolder))
                    .map(include -> "#include \"" + include + "\"")
                    .collect(Collectors.joining("\n"));
    
            if (includeCode.length() != 0) {
                code.append(includeCode).append("\n");
            }
    
        }
    
        // If there is code, write and return file
        if (code.length() != 0) {
            File tempFolder = SpecsIo.mkdir(TEMP_SRC_FOLDER);
            SpecsIo.deleteFolderContents(tempFolder, true);
    
            String extension = args.get(ClavaOptions.STANDARD).getImplementionExtension();
    
            File implementationFile = new File(tempFolder, "implementation." + extension);
            SpecsIo.write(implementationFile, code.toString());
    
            return Arrays.asList(implementationFile.getAbsolutePath());
        }
    
        throw new RuntimeException("Could not find C/C++ files in the given source folders (" + sources + ")");
    }
    */
    private List<String> processSources(Map<String, File> sourceFiles) {
        SpecsCheck.checkArgument(!sourceFiles.isEmpty(),
                () -> "No C/C++ files found in the given source folders:" + getSources());

        List<String> implementationFiles = sourceFiles.keySet().stream()
                .filter(SourceType.IMPLEMENTATION::hasExtension)
                .collect(Collectors.toList());

        // List<String> sourceFiles = SpecsIo.getFiles(sources, SourceType.IMPLEMENTATION.getExtensions());

        if (!implementationFiles.isEmpty()) {
            return implementationFiles;
        }

        // No implementation files found. Create temporary file with only the includes

        StringBuilder code = new StringBuilder();
        for (Entry<String, File> sourceFile : sourceFiles.entrySet()) {
            // If not an header file, ignore
            if (!SourceType.HEADER.hasExtension(sourceFile.getKey())) {
                continue;
            }

            String includePath = SpecsIo.getRelativePath(new File(sourceFile.getKey()), sourceFile.getValue());
            String includeCode = "#include \"" + includePath + "\"";
            code.append(includeCode).append("\n");
        }

        /*
        for (File sourceFolder : sources) {
            // List<File> headerFiles = SpecsIo.getFilesRecursive(sourceFolder, App.getExtensionsHeaders());
            List<File> headerFiles = SpecsIo.getFilesRecursive(sourceFolder, SourceType.HEADER.getExtensions());
        
            // Create source code to call header
            String includeCode = headerFiles.stream()
                    .map(file -> SpecsIo.getRelativePath(file, sourceFolder))
                    .map(include -> "#include \"" + include + "\"")
                    .collect(Collectors.joining("\n"));
        
            if (includeCode.length() != 0) {
                code.append(includeCode).append("\n");
            }
        
        }
        */

        Preconditions.checkArgument(code.length() != 0, "Expected to find at least one header file:" + sourceFiles);

        // Write and return file
        File tempFolder = SpecsIo.mkdir(TEMP_SRC_FOLDER);
        SpecsIo.deleteFolderContents(tempFolder, true);

        String extension = args.get(ClavaOptions.STANDARD).getImplementionExtension();

        File implementationFile = new File(tempFolder, "implementation." + extension);
        SpecsIo.write(implementationFile, code.toString());

        return Arrays.asList(implementationFile.getAbsolutePath());

    }

    /**
     * Return a JoinPoint instance of the language root, i.e., an instance of AProgram
     *
     * @return an instance of the join point root/program
     */
    @Override
    public JoinPoint select() {
        return CxxJoinpoints.create(getApp(), null);
        // return new CxxProgram(getProgramName(), app, this);
    }

    public String getProgramName() {
        return getFirstSourceFolder(getSources()).getName();
        // return getSources().stream()
        // .findFirst()
        // .map(File::getName)
        // .orElse("C/C++ Program");
        // return baseFolder.getName();
    }

    public File getWeavingFolder() {
        // return SpecsIo.mkdir(outputDir, args.get(CxxWeaverOption.WOVEN_CODE_FOLDERNAME));
        return SpecsIo.mkdir(args.get(LaraiKeys.OUTPUT_FOLDER), args.get(CxxWeaverOption.WOVEN_CODE_FOLDERNAME));
    }

    /**
     * Closes the weaver to the specified output directory location, if the weaver generates new file(s)
     *
     * @return if close was successful
     */
    @Override
    public boolean close() {
        if (!args.get(CxxWeaverOption.DISABLE_WEAVING)) {
            if (args.get(CxxWeaverOption.CHECK_SYNTAX)) {
                SpecsLogs.msgInfo("Checking woven code syntax...");
                rebuildAst(false);
            }

            // Terminate weaver execution with final steps required and writing output files

            // Write output files if code generation is not disabled
            if (!args.get(CxxWeaverOption.DISABLE_CODE_GENERATION)) {
                writeCode(getWeavingFolder());
            }

        }

        /// Clean-up phase

        // Delete temporary weaving folder, if exists
        SpecsIo.deleteFolder(new File(TEMP_WEAVING_FOLDER));

        // Delete temporary source folder, if exists
        SpecsIo.deleteFolder(new File(TEMP_SRC_FOLDER));

        // Delete intermediary files
        if (args.get(CxxWeaverOption.CLEAN_INTERMEDIATE_FILES)) {
            for (String tempFile : ClangAstParser.getTempFiles()) {
                new File(tempFile).delete();
            }
        }

        // Re-enable output
        // TODO: Temporary solution, while we do not use SpecsLoggers
        if (args.get(CxxWeaverOption.DISABLE_CLAVA_INFO)) {
            Preconditions.checkNotNull(infoLogger);
            infoLogger.setLevel(previousLevel);
            infoLogger = null;
            previousLevel = null;
        }

        if (!messagesToUser.isEmpty()) {
            ClavaLog.info(" - Messages -");
            messagesToUser.forEach(ClavaLog::info);
        }

        // Clear weaver data
        weaverData = null;

        return true;
    }

    public void writeCode(File outputFolder) {
        Set<String> modifiedFilenames = getModifiedFilenames();

        boolean flattenFolders = getConfig().get(CxxWeaverOption.FLATTEN_WOVEN_CODE_FOLDER_STRUCTURE);

        // Get files and contents to write
        Map<File, String> files = getApp().getCode(outputFolder, flattenFolders, modifiedFilenames);

        // Write files that have changed
        for (Entry<File, String> entry : files.entrySet()) {
            File destinationFile = entry.getKey();
            String code = entry.getValue();

            // If file already exists, and is the same as the file that we are about to write, skip
            if (destinationFile.isFile() && areEqual(destinationFile, code)) {
                continue;
            }

            SpecsLogs.msgInfo("Changes in file '" + destinationFile + "'");
            SpecsIo.write(destinationFile, code);
        }
    }

    private Set<String> getModifiedFilenames() {
        // If option is disabled, return null
        if (!args.get(CxxWeaverOption.GENERATE_MODIFIED_CODE_ONLY)) {
            return null;
        }

        // Option is enabled, get list of filenames to generate from AST
        return modifiedFilesGear.getModifiedFilenames();
    }

    private static boolean areEqual(File expected, String actual) {

        // Case where the file is empty
        if (expected.length() == 0) {
            return actual.isEmpty();
        }

        try (LineStream original = LineStream.newInstance(expected)) {
            StringLines codeLines = StringLines.newInstance(actual);

            long counter = 0;
            while (original.hasNextLine()) {
                counter++;

                if (!codeLines.hasNextLine()) {
                    SpecsLogs
                            .msgLib(
                                    "Expected has line but actual has no more lines.\nExpected:" + original.nextLine());
                    return false;
                }

                String expectedLine = original.nextLine();
                String actualLine = codeLines.nextLine();

                if (!expectedLine.equals(actualLine)) {
                    SpecsLogs.msgLib(
                            "Difference on line " + counter + ".\nActual:" + actualLine + "\nExpected:" + expectedLine);
                    return false;
                }
            }

            // Check if there are still other lines
            if (codeLines.hasNextLine()) {
                return false;
            }

        }

        return true;
    }

    /**
     * Returns a list of Gears associated to this weaver engine
     *
     * @return a list of implementations of {@link AGear} or null if no gears are available
     */
    @Override
    public List<AGear> getGears() {
        return Arrays.asList(modifiedFilesGear, insideApplyGear);
    }

    @Override
    public List<WeaverOption> getOptions() {
        return CxxWeaverOption.STORE_DEFINITION.getKeys().stream()
                .map(CxxWeaverOptions::getOption)
                .collect(Collectors.toList());

    }

    @Override
    public LanguageSpecification getLanguageSpecification() {
        return buildLanguageSpecification();
    }

    @Override
    public String getName() {
        // v1.2.2
        return "Clava";
    }

    public DataStore getConfig() {
        return args;
    }

    /**
     *
     * @param update
     *            if true, the weaver will update its state to use the rebuilt tree instead of the original tree
     */
    public void rebuildAst(boolean update) {
        // Check if inside apply

        // Write current tree to a temporary folder
        File tempFolder = SpecsIo.mkdir(TEMP_WEAVING_FOLDER);
        SpecsIo.deleteFolderContents(tempFolder, true);

        boolean flattenFolders = getConfig().get(CxxWeaverOption.FLATTEN_WOVEN_CODE_FOLDER_STRUCTURE);

        getApp().write(tempFolder, flattenFolders);

        // For all Translation Units, collect new destination folders
        List<File> srcFolders = getApp().getTranslationUnits().stream()
                .map(tu -> tu.getDestinationFolder(tempFolder, flattenFolders))
                .collect(Collectors.toList());

        // TODO: When separation of src/include is done, take it into account here
        // List<File> srcFolders = SpecsCollections.concat(tempFolder, SpecsIo.getFoldersRecursive(tempFolder));
        List<File> includeFolders = srcFolders;

        // Copy current options
        List<String> rebuildOptions = new ArrayList<>(parserOptions);

        // Add include folders
        for (File includeFolder : includeFolders) {
            rebuildOptions.add("\"-I" + includeFolder.getAbsolutePath() + "\"");
        }

        App rebuiltApp = createApp(srcFolders, rebuildOptions);

        // System.out.println("TUs:"
        // + getApp().getTranslationUnits().stream().map(tu -> tu.getFilename())
        // .collect(Collectors.toList()));
        //
        // System.out.println("TUs Rebuilt:"
        // + rebuiltApp.getTranslationUnits().stream().map(tu -> tu.getFilename())
        // .collect(Collectors.toList()));

        // Base folder is now the temporary folder
        if (update) {
            // Discard current app
            weaverData.popAst();

            // Add rebuilt app
            weaverData.pushAst(rebuiltApp);

            // TODO: When separation of src/include is done, update accordingly
            currentSources = srcFolders;

            // baseFolder = tempFolder;
        }

        // Clear user values, all stored nodes are invalid now
        // userValues = new HashMap<>();
        // Discard user values
        // userValuesStack.pop();
        // userValuesStack.push(new HashMap<>());
    }

    @Override
    public List<Class<?>> getImportableClasses() {
        return CLAVA_IMPORTABLE_CLASSES;

    }

    @Override
    public List<ResourceProvider> getAspectsAPI() {
        return CLAVA_LARA_API;
    }

    @Override
    public List<ResourceProvider> getImportableScripts() {
        return ResourceProvider.getResourcesFromEnum(Arrays.asList(JsApiResource.class, JsAntarexApiResource.class));
    }

    @Override
    public ResourceProvider getIcon() {
        // return () -> "cxxweaver/clava_icon_48x48.ico";
        return () -> "clava/clava_icon_300dpi.png";
    }

    public Object getUserField(ClavaNode node, String fieldName) {
        // Get node values
        Map<String, Object> values = getUserValues().get(node);
        if (values == null) {
            return null;
        }

        return values.get(fieldName);
    }

    public Object setUserField(ClavaNode node, String fieldName, Object value) {
        // Get node values
        Map<String, Object> values = getUserValues().get(node);

        // Initialize map if null
        if (values == null) {
            values = new HashMap<>();
            getUserValues().put(node, values);
        }

        return values.put(fieldName, value);
    }

    public List<File> getSources() {
        return currentSources;
    }

    public boolean clearUserField(ClavaNode node) {
        return getUserValues().remove(node) != null;
    }

    public static CxxWeaver getCxxWeaver() {
        return (CxxWeaver) getThreadLocalWeaver();
    }

    @Override
    public Set<String> getLanguages() {
        return LANGUAGES;
    }

    public void pushAst() {
        // Create a copy of app and push it
        App clonedApp = (App) getApp().copy();
        weaverData.pushAst(clonedApp);
    }

    public void popAst() {
        // Discard app and user values
        weaverData.popAst();
    }

    public Integer nextId(String prefix) {

        return accMap.add(prefix);
    }

    public List<Include> getAvailableIncludes() {
        // Search on sources and normal includes

        List<File> searchPaths = new ArrayList<>();
        searchPaths.addAll(currentSources);
        searchPaths.addAll(args.get(CxxWeaverOption.HEADER_INCLUDES).getFiles());
        // System.out.println("SEARCH PATHS:" + searchPaths);
        Set<String> includeFolders = searchPaths.stream()
                // .map(CxxWeaver::parseIncludePath)
                .map(path -> path.isFile() ? path.getParentFile().getAbsolutePath() : path.getAbsolutePath())
                .collect(Collectors.toSet());

        // System.out.println("INCLUDE PATHS:" + includeFolders);

        List<Include> includes = new ArrayList<>();

        Set<String> seenIncludes = new HashSet<>();
        for (String includeFolderName : includeFolders) {
            File includeFolder = new File(includeFolderName);

            // Get all files from folder
            // List<File> currentIncludes = SpecsIo.getFilesRecursive(includeFolder,
            // TranslationUnit.getHeaderExtensions());
            List<File> currentIncludes = SpecsIo.getFilesRecursive(includeFolder,
                    SourceType.HEADER.getExtensions());

            currentIncludes.stream()
                    // Filter already added includes
                    .filter(path -> seenIncludes.add(SpecsIo.getCanonicalPath(path)))
                    .map(currentInclude -> new Include(currentInclude,
                            SpecsIo.getRelativePath(currentInclude, includeFolder), -1, false))
                    .forEach(include -> includes.add(include));
        }

        return includes;
    }

    // public static String getRelativeFilepath(TranslationUnit tunit) {
    // return tunit.getRelativeFilepath(CxxWeaver.getCxxWeaver().getBaseSourceFolder());
    // }

    // public static String getRelativeFolderpath(TranslationUnit tunit) {
    // return tunit.getRelativeFolderpath(CxxWeaver.getCxxWeaver().getBaseSourceFolder());
    // }
}
