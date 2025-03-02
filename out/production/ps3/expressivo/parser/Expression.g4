/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

/**
 * This file is the grammar file used by ANTLR.
 *
 * In order to compile this file, navigate to this folder
 * (src/expressivo/parser) and run the following command:
 * 
 * java -jar ../../../lib/antlr.jar Expression.g4
 *
 * PS3 instructions: you are free to change this grammar.
 */

grammar Expression;
import Configuration;

/*
 * Nonterminal rules (a.k.a. parser rules) must be lowercase, e.g. "root" and "sum" below.
 *
 * Terminal rules (a.k.a. lexical rules) must be UPPERCASE, e.g. NUMBER below.
 * Terminals can be defined with quoted strings or regular expressions.
 *
 * You should make sure you have one rule that describes the entire input.
 * This is the "start rule". The start rule should end with the special token
 * EOF so that it describes the entire input. Below, "root" is the start rule.
 *
 * For more information, see reading 18 about parser generators, which explains
 * how to use Antlr and has links to reference information.
 */
root : sum EOF;  // 入口规则，整条表达式必须完全匹配

// 加法：低优先级
sum : product ('+' product)*;

// 乘法：高优先级
product : primitive ('*' primitive)*;

// 处理数字和括号
primitive : NUMBER | VARIABLE | '(' sum ')';

// 数字定义
NUMBER : [0-9]+ ('.' [0-9]+)?;

// 变量定义
VARIABLE: [a-zA-Z]+;

/* Tell Antlr to ignore spaces around tokens. */
SPACES: [ ]+ -> skip;
