package com.karthik.pretty_compiler

import javax.lang.model.element.Element

class ProcessingException(val element:Element,
                          msg:String): Exception(msg)