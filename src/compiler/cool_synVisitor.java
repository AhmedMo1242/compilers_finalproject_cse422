// Generated from cool_syn.g4 by ANTLR 4.13.2
package compiler;
// Generated from D:/compilers/compilers_finalproject_cse422/cool_syn.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link cool_synParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface cool_synVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code start}
	 * labeled alternative in {@link cool_synParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(cool_synParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classdef}
	 * labeled alternative in {@link cool_synParser#class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassdef(cool_synParser.ClassdefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function}
	 * labeled alternative in {@link cool_synParser#feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(cool_synParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link cool_synParser#feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(cool_synParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parameter}
	 * labeled alternative in {@link cool_synParser#formal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(cool_synParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code objectcall}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectcall(cool_synParser.ObjectcallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sub}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSub(cool_synParser.SubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mul}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul(cool_synParser.MulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLq(cool_synParser.LqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code num}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum(cool_synParser.NumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lt}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLt(cool_synParser.LtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(cool_synParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code div}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiv(cool_synParser.DivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(cool_synParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code block}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(cool_synParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code let}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet(cool_synParser.LetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(cool_synParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code text}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(cool_synParser.TextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code newobject}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewobject(cool_synParser.NewobjectContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(cool_synParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code case}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase(cool_synParser.CaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parexpr}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParexpr(cool_synParser.ParexprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code add}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(cool_synParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code void}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoid(cool_synParser.VoidContext ctx);
	/**
	 * Visit a parse tree produced by the {@code invert}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvert(cool_synParser.InvertContext ctx);
	/**
	 * Visit a parse tree produced by the {@code false}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(cool_synParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGq(cool_synParser.GqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eq}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq(cool_synParser.EqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gt}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGt(cool_synParser.GtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code true}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(cool_synParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code staticcall}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticcall(cool_synParser.StaticcallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link cool_synParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(cool_synParser.AssignContext ctx);
}