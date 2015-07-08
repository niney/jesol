grails.plugin.springsecurity.interceptUrlMap = [
//        '/**/**':                  ['ROLE_ADMIN'],
//        '/admin/*':           ['ROLE_ADMIN'],
        '/':               ['permitAll'],
        '/index':          ['permitAll'],
        '/index.gsp':      ['permitAll'],
        '/assets/**':      ['permitAll'],
        '/**/js/**':       ['permitAll'],
        '/**/css/**':      ['permitAll'],
        '/**/images/**':   ['permitAll'],
        '/**/favicon.ico': ['permitAll'],
        // angular
        '/index.html':          ['permitAll'],
        '/**/scripts/**':       ['permitAll'],
        '/**/styles/**':      ['permitAll'],
        '/**/fonts/**':   ['permitAll'],
        '/**/views/**':   ['permitAll'],
        '/**/**':                  ['ROLE_ADMIN']
//        '/**/**':                  ['permitAll']
]