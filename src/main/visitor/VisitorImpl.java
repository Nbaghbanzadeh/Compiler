package main.visitor;
import main.ast.node.*;
import main.ast.node.Program;
import main.ast.node.declaration.*;
import main.ast.node.declaration.handler.*;
import main.ast.node.declaration.VarDeclaration;
import main.ast.node.expression.*;
import main.ast.node.expression.values.BooleanValue;
import main.ast.node.expression.values.IntValue;
import main.ast.node.expression.values.StringValue;
import main.ast.node.statement.*;
import main.symbolTable.SymbolTable;

import java.util.HashSet;
import java.util.Set;

public class VisitorImpl implements Visitor {
    private Set<String> actorNames = new HashSet<>();
    private Set<String> knownActorNames = new HashSet<>();
    private Set<String> actorVarNames = new HashSet<>();
    private Set<String> msgHandlerNames = new HashSet<>();
    private Set<String> msgHandlerArgsNames = new HashSet<>();
    private Set<String> msgHandlerLocalVarsNames = new HashSet<>();

    protected void visitStatement( Statement stat )
    {
        if( stat == null )
            return;
        else if( stat instanceof MsgHandlerCall )
            this.visit( ( MsgHandlerCall ) stat );
        else if( stat instanceof Block )
            this.visit( ( Block ) stat );
        else if( stat instanceof Conditional )
            this.visit( ( Conditional ) stat );
        else if( stat instanceof For )
            this.visit( ( For ) stat );
        else if( stat instanceof Break )
            this.visit( ( Break ) stat );
        else if( stat instanceof Continue )
            this.visit( ( Continue ) stat );
        else if( stat instanceof Print )
            this.visit( ( Print ) stat );
        else if( stat instanceof Assign )
            this.visit( ( Assign ) stat );
    }

    protected void visitExpr( Expression expr )
    {
        if( expr == null )
            return;
        else if( expr instanceof UnaryExpression )
            this.visit( ( UnaryExpression ) expr );
        else if( expr instanceof BinaryExpression )
            this.visit( ( BinaryExpression ) expr );
        else if( expr instanceof ArrayCall )
            this.visit( ( ArrayCall ) expr );
        else if( expr instanceof ActorVarAccess )
            this.visit( ( ActorVarAccess ) expr );
        else if( expr instanceof Identifier )
            this.visit( ( Identifier ) expr );
        else if( expr instanceof Self )
            this.visit( ( Self ) expr );
        else if( expr instanceof Sender )
            this.visit( ( Sender ) expr );
        else if( expr instanceof BooleanValue )
            this.visit( ( BooleanValue ) expr );
        else if( expr instanceof IntValue )
            this.visit( ( IntValue ) expr );
        else if( expr instanceof StringValue )
            this.visit( ( StringValue ) expr );
    }

    @Override
    public void visit(Program program) {
        for (ActorDeclaration actor: program.getActors()) {
            actor.accept(this);
            actorNames.add(actor.getName().getName());
        }
        program.getMain().accept(this);
    }

    @Override
    public void visit(ActorDeclaration actorDeclaration) {
        for (VarDeclaration knownActor : actorDeclaration.getKnownActors()) {
            knownActorNames.add(actorDeclaration.getName().getName() + "_" +
                    knownActor.getIdentifier().getName() + "_" + knownActor.getType());
            knownActor.accept(this);
        }
        for (VarDeclaration actorVar : actorDeclaration.getActorVars()) {
            actorVarNames.add(actorDeclaration.getName().getName() + "_" +
                    actorVar.getIdentifier().getName() + "_" + actorVar.getType());
            actorVar.accept(this);
        }
        if(actorDeclaration.getInitHandler() != null) { //////////////////////////////////////
            msgHandlerNames.add(actorDeclaration.getName().getName() + "_" +
                    actorDeclaration.getInitHandler().getName().getName());
            actorDeclaration.getInitHandler().accept(this);
        }
        for (HandlerDeclaration msgHandler : actorDeclaration.getMsgHandlers()) {
            msgHandlerNames.add(actorDeclaration.getName().getName() + "_" +
                    msgHandler.getName().getName());
            msgHandler.accept(this);
        }
    }

    @Override
    public void visit(HandlerDeclaration handlerDeclaration) {
        for (VarDeclaration arg : handlerDeclaration.getArgs()) {
            msgHandlerArgsNames.add(handlerDeclaration.getName().getName()
                    + "_" + arg.getIdentifier().getName() + "_" + arg.getType());
            arg.accept(this);
        }
        for (VarDeclaration localVar : handlerDeclaration.getLocalVars()) {
            msgHandlerLocalVarsNames.add(handlerDeclaration.getName().getName()
                    + "_" + localVar.getIdentifier().getName() + "_" + localVar.getType());
            localVar.accept(this);
        }
        for (Statement body : handlerDeclaration.getBody()) {
            body.accept(this);
        }
    }

    @Override
    public void visit(VarDeclaration varDeclaration) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Main mainActors) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(ActorInstantiation actorInstantiation) {
        //TODO: implement appropriate visit functionality
    }


    @Override
    public void visit(UnaryExpression unaryExpression) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(BinaryExpression binaryExpression) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(ArrayCall arrayCall) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(ActorVarAccess actorVarAccess) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Identifier identifier) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Self self) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Sender sender) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(BooleanValue value) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(IntValue value) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(StringValue value) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Block block) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Conditional conditional) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(For loop) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Break breakLoop) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Continue continueLoop) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(MsgHandlerCall msgHandlerCall) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Print print) {
        //TODO: implement appropriate visit functionality
    }

        @Override
    public void visit(Assign assign) {
        //TODO: implement appropriate visit functionality
    }
}
