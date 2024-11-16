require libxml2.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/libxml2:"

inherit ptest python_setuptools_build_meta

DEPENDS += "libxml2"

PEP517_SOURCE_PATH = "${S}/python"

#EXTRA_OECONF += "--with-python"

RDEPENDS:${PN}-ptest += "bash make locale-base-en-us libgcc python3-core python3-logging python3-shell python3-stringold python3-threading python3-unittest"

RDEPENDS:${PN}-ptest:append:libc-musl = " musl-locales"
RDEPENDS:${PN}-ptest:append:libc-glibc = " glibc-gconv-ebcdic-us \
                                           glibc-gconv-ibm1141 \
                                           glibc-gconv-iso8859-5 \
                                           glibc-gconv-euc-jp \
                                         "