import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.lang.StringUtils;

def dir = new File(project.properties['target'])
if (!dir.exists()) {
    log.info("Directory is absent: " + dir)
    return
}
def it = FileUtils.iterateFiles(
    dir,
    new SuffixFileFilter(".html"),
    TrueFileFilter.INSTANCE
)
def find = StringUtils.stripAll(
    StringUtils.split(project.properties['find'], "\n")
)
def replace = StringUtils.stripAll(
    StringUtils.split(project.properties['replace'], "\n")
)
while (it.hasNext()) {
    def File file = it.next()
    def contents = FileUtils.readFileToString(file)
    contents = StringUtils.replaceEach(contents, find, replace)
    FileUtils.writeStringToFile(file, contents)
}
