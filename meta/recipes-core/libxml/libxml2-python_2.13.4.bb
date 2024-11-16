require libxml2.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/libxml2:"

inherit autotools pkgconfig binconfig-disabled ptest


#do_fetch[noexec] = "1"
#do_unpack[noexec] = "1"

#S = "${WORKDIR}/libxml2-${PV}"

inherit_defer ${@bb.utils.contains('PACKAGECONFIG', 'python', 'python3targetconfig', '', d)}

EXTRA_OECONF += "--with-python"

RDEPENDS:${PN}-ptest += "bash make locale-base-en-us libgcc python3-core python3-logging python3-shell python3-stringold python3-threading python3-unittest ${PN}-python"

RDEPENDS:${PN}-python += "${@bb.utils.contains('PACKAGECONFIG', 'python', 'python3-core', '', d)}"

RDEPENDS:${PN}-ptest:append:libc-musl = " musl-locales"
RDEPENDS:${PN}-ptest:append:libc-glibc = " glibc-gconv-ebcdic-us \
                                           glibc-gconv-ibm1141 \
                                           glibc-gconv-iso8859-5 \
                                           glibc-gconv-euc-jp \
                                         "