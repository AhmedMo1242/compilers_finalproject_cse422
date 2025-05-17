// Generated from D:/compilers/compilers_finalproject_cse422/cool_syn.g4 by ANTLR 4.13.2
package compiler;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link cool_synParser}.
 */
public interface cool_synListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code start}
	 * labeled alternative in {@link cool_synParser#program}.
	 * @param ctx the parse tree
	 */
	void enterStart(cool_synParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code start}
	 * labeled alternative in {@link cool_synParser#program}.
	 * @param ctx the parse tree
	 */
	void exitStart(cool_synParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classdef}
	 * labeled alternative in {@link cool_synParser#class}.
	 * @param ctx the parse tree
	 */
	void enterClassdef(cool_synParser.ClassdefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classdef}
	 * labeled alternative in {@link cool_synParser#class}.
	 * @param ctx the parse tree
	 */
	void exitClassdef(cool_synParser.ClassdefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function}
	 * labeled alternative in {@link cool_synParser#feature}.
	 * @param ctx the parse tree
	 */
	void enterFunction(cool_synParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function}
	 * labeled alternative in {@link cool_synParser#feature}.
	 * @param ctx the parse tree
	 */
	void exitFunction(cool_synParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variable}
	 * labeled alternative in {@link cool_synParser#feature}.
	 * @param ctx the parse tree
	 */
	void enterVariable(cool_synParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link cool_synParser#feature}.
	 * @param ctx the parse tree
	 */
	void exitVariable(cool_synParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link cool_synParser#formal}.
	 * @param ctx the parse tree
	 */
	void enterParameter(cool_synParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link cool_synParser#formal}.
	 * @param ctx the parse tree
	 */
	void exitParameter(cool_synParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by the {@code objectcall}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterObjectcall(cool_synParser.ObjectcallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code objectcall}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitObjectcall(cool_synParser.ObjectcallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sub}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSub(cool_synParser.SubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sub}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSub(cool_synParser.SubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mul}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMul(cool_synParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mul}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMul(cool_synParser.MulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLq(cool_synParser.LqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLq(cool_synParser.LqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code num}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNum(cool_synParser.NumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code num}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNum(cool_synParser.NumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lt}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLt(cool_synParser.LtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lt}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLt(cool_synParser.LtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code while}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterWhile(cool_synParser.WhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code while}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitWhile(cool_synParser.WhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code div}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDiv(cool_synParser.DivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code div}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDiv(cool_synParser.DivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code not}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNot(cool_synParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code not}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNot(cool_synParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBlock(cool_synParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBlock(cool_synParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code let}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLet(cool_synParser.LetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code let}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLet(cool_synParser.LetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(cool_synParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(cool_synParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code text}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterText(cool_synParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code text}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitText(cool_synParser.TextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newobject}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNewobject(cool_synParser.NewobjectContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newobject}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNewobject(cool_synParser.NewobjectContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIf(cool_synParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIf(cool_synParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code case}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCase(cool_synParser.CaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code case}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCase(cool_synParser.CaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parexpr}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParexpr(cool_synParser.ParexprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parexpr}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParexpr(cool_synParser.ParexprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code add}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAdd(cool_synParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code add}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAdd(cool_synParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code void}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVoid(cool_synParser.VoidContext ctx);
	/**
	 * Exit a parse tree produced by the {@code void}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVoid(cool_synParser.VoidContext ctx);
	/**
	 * Enter a parse tree produced by the {@code invert}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInvert(cool_synParser.InvertContext ctx);
	/**
	 * Exit a parse tree produced by the {@code invert}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInvert(cool_synParser.InvertContext ctx);
	/**
	 * Enter a parse tree produced by the {@code false}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFalse(cool_synParser.FalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code false}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFalse(cool_synParser.FalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGq(cool_synParser.GqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGq(cool_synParser.GqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEq(cool_synParser.EqContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEq(cool_synParser.EqContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gt}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGt(cool_synParser.GtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gt}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGt(cool_synParser.GtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code true}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTrue(cool_synParser.TrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code true}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTrue(cool_synParser.TrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code staticcall}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStaticcall(cool_synParser.StaticcallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code staticcall}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStaticcall(cool_synParser.StaticcallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign(cool_synParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign(cool_synParser.AssignContext ctx);
}