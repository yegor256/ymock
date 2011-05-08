def file = new File(project.properties['xml'])
if (!file.exists()) {
    log.info("No FindBugs XML report, nothing to parse")
    return
}
def xml = new XmlParser().parse(file)
def bugs = xml.BugInstance
def total = bugs.size()
if (total <= 0) { log.info("No errors/warnings found"); return
}
log.info("Total bugs: " + total)
for (i in 0..total-1) {
    def bug = bugs[i]
    log.info(
        bug.LongMessage.text()
        + " " + bug.Class.'@classname'
        + " " + bug.Class.SourceLine.Message.text()
    )
}
if (project.properties['failOnError']) {
    fail(total + " FindBugs error(s)")
}
