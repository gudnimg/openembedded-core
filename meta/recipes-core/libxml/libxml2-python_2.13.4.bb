require libxml2.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/libxml2:"

#SRC_URI += "${GNOME_MIRROR}/${GNOMEBN}/${@oe.utils.trim_version("${PV}", 2)}/${GNOMEBN}-${PV}.tar.${GNOME_COMPRESS_TYPE};name=archive"

inherit ptest python3targetconfig

DEPENDS += "libxml2"

S = "${WORKDIR}/libxml2-${PV}"

#PEP517_SOURCE_PATH = "${S}/python"

EXTRA_OEMAKE = "-C ${B}/python"

#do_compile() {
#    oe_runmake -C "${S}/python"
#}

RDEPENDS:${PN}:append = " libxml2"

RDEPENDS:${PN}-ptest += "bash make locale-base-en-us libgcc python3-core python3-logging python3-shell python3-stringold python3-threading python3-unittest"

RDEPENDS:${PN}-ptest:append:libc-musl = " musl-locales"
RDEPENDS:${PN}-ptest:append:libc-glibc = " glibc-gconv-ebcdic-us \
                                           glibc-gconv-ibm1141 \
                                           glibc-gconv-iso8859-5 \
                                           glibc-gconv-euc-jp \
                                         "