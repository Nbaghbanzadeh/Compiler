package main.visitor;
import main.ast.node.*;
import main.ast.node.Program;
import main.ast.node.declaration.*;
import main.ast.node.declaration.handler.*;
import main.ast.node.declaration.VarDeclaration;
import main.ast.node.expression.*;
import main.ast.node.expression.operators.BinaryOperator;
import main.ast.node.expression.operators.UnaryOperator;
import main.ast.node.expression.values.BooleanValue;
import main.ast.node.expression.values.IntValue;
import main.ast.node.expression.values.StringValue;
import main.ast.node.statement.*;
import java.util.*;

import main.ast.type.Type;
import main.ast.type.actorType.ActorType;
import main.ast.type.arrayType.ArrayType;
import main.ast.type.noType.NoType;
import main.ast.type.primitiveType.BooleanType;
import main.ast.type.primitiveType.IntType;
import main.ast.type.primitiveType.StringType;


public class VisitorImpl implements Visitor {
    private ArrayList<String> errorMessages = new ArrayList<String>();
    private ArrayList<String> actorNames = new ArrayList<String>();
    private ArrayList<String> knownActorNames = new ArrayList<String>();
    private ArrayList<String> actorVarNames = new ArrayList<String>();
    private ArrayList<String> msgHandlerNames = new ArrayList<String>();
    private ArrayList<String> msgHandlerNames2 = new ArrayList<String>();
    private ArrayList<String> msgHandlerArgsNames = new ArrayList<String>();
    private ArrayList<String> msgHandlerLocalVarsNames = new ArrayList<String>();
    private ArrayList<String> blocks = new ArrayList<String>();
    private ArrayList<String> varDeclarationNames = new ArrayList<String>();
    private ArrayList<String> msgHandlerArgsNames2 = new ArrayList<String>();
    private ArrayList<String> parents = new ArrayList<String>();
    private int blockCounter = 0;
    private int globalThisBlock = 0;
    private String currentActor = "";
    private String currentHandler = "";

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

    public boolean isSubtype(Type right, Type left) {
        if(left.toString().equals(right.toString())){
            return true;
        }
        if(right.toString().equals("notype")) {
            return true;
        }
        return false;
    }

    public Type returnType(String toString, Identifier id) {
        if(toString.equals("int"))
            return new IntType();
        else if(toString.equals("boolean"))
            return new BooleanType();
        else if(toString.equals("int[]"))
            return new ArrayType(1);
        else if(toString.equals("string"))
            return new StringType();
        else
            return new NoType();
    }

    public void firstOfAll(Program program) {
        ArrayList<String> knownActorNamesTemp = new ArrayList<String>();
        ArrayList<String> actorVarNamesTemp = new ArrayList<String>();
        ArrayList<String> msgHandlerNamesTemp = new ArrayList<String>();
        ArrayList<String> msgHandlerArgsNamesTemp = new ArrayList<String>();
        ArrayList<String> msgHandlerLocalVarsNamesTemp = new ArrayList<String>();

        for (ActorDeclaration actorDeclaration: program.getActors()) {
            currentActor = actorDeclaration.getName().getName();
            actorNames.add(actorDeclaration.getName().getName());
            if(actorDeclaration.getParentName() != null)
                parents.add(actorDeclaration.getName().getName() + "#" + actorDeclaration.getParentName().getName());
            else
                parents.add(actorDeclaration.getName().getName() + "#$noParent");
            for (VarDeclaration knownActor : actorDeclaration.getKnownActors()) {
                knownActorNames.add(actorDeclaration.getName().getName() + "#" +
                        knownActor.getIdentifier().getName() + "#" + knownActor.getType().toString());
            }
            for (VarDeclaration actorVar : actorDeclaration.getActorVars()) {
                actorVarNames.add(actorDeclaration.getName().getName() + "#" +
                        actorVar.getIdentifier().getName() + "#" + actorVar.getType().toString());
            }
            if(actorDeclaration.getInitHandler() != null) { //////////////////////////////////////
                HandlerDeclaration handlerDeclaration = actorDeclaration.getInitHandler();
                for (VarDeclaration arg : handlerDeclaration.getArgs()) {
                    msgHandlerArgsNames.add(handlerDeclaration.getName().getName()
                            + "#" + arg.getType().toString() + "#"  + currentActor);
                    msgHandlerArgsNames2.add(handlerDeclaration.getName().getName()
                            + "#" + arg.getIdentifier().getName() + "#"  + currentActor + "#" + arg.getType().toString());
                }
                for (VarDeclaration localVar : handlerDeclaration.getLocalVars()) {
                    msgHandlerLocalVarsNames.add(handlerDeclaration.getName().getName()
                            + "#" + localVar.getIdentifier().getName() + "#" + localVar.getType().toString() + "#" + currentActor);
                }
                msgHandlerNames.add(actorDeclaration.getName().getName() + "#" +
                        actorDeclaration.getInitHandler().getName().getName());
            }
            for (HandlerDeclaration handlerDeclaration : actorDeclaration.getMsgHandlers()) {
                for (VarDeclaration arg : handlerDeclaration.getArgs()) {
                    msgHandlerArgsNames.add(handlerDeclaration.getName().getName()
                            + "#" + arg.getType().toString() + "#"  + currentActor);
                    msgHandlerArgsNames2.add(handlerDeclaration.getName().getName()
                            + "#" + arg.getIdentifier().getName() + "#"  + currentActor + "#" + arg.getType().toString());
                }
                for (VarDeclaration localVar : handlerDeclaration.getLocalVars()) {
                    msgHandlerLocalVarsNames.add(handlerDeclaration.getName().getName()
                            + "#" + localVar.getIdentifier().getName() + "#" + localVar.getType().toString() + "#" + currentActor);
                }
                msgHandlerNames.add(actorDeclaration.getName().getName() + "#" +
                        handlerDeclaration.getName().getName());
            }
        }
        for (ActorDeclaration actorDeclaration: program.getActors()) {
            for (String str : parents) {
                if(str.split("#", 2)[0].equals(actorDeclaration.getName().getName())) {
                    String now = str;
                    String man = now.split("#", 2)[0];
                    while (true) {
                        if(!(actorNames.contains(now.split("#", 2)[1])))
                            break;
                        for (String str1 : knownActorNames) { //add it's parent's knownActors to the lists
                            if(str1.split("#", 2)[0].equals(now.split("#", 2)[1])) {
                                knownActorNamesTemp.add(man +
                                        "#" + str1.split("#", 2)[1]);
                            }
                        }
                        for (String str1 : knownActorNamesTemp) {
                            knownActorNames.add(str1);
                        }
                        knownActorNamesTemp.clear();
                        for (String str1 : actorVarNames) { //add it's parent's actorVars to the lists
                            if(str1.split("#", 2)[0].equals(now.split("#", 2)[1])) {
                                actorVarNamesTemp.add(man +
                                        "#" + str1.split("#", 2)[1]);
                            }
                        }
                        for (String str1 : actorVarNamesTemp) {
                            actorVarNames.add(str1);
                        }
                        actorVarNamesTemp.clear();
                        for (String str1 : msgHandlerNames) { //add it's parent's msgHandlers to the lists
                            if(str1.split("#", 2)[0].equals(now.split("#", 2)[1])) {
                                msgHandlerNamesTemp.add(man +
                                        "#" + str1.split("#", 2)[1]);
                            }
                        }
                        for (String str1 : msgHandlerNamesTemp) {
                            msgHandlerNames.add(str1);
                        }
                        msgHandlerNamesTemp.clear();
                        for (String str1 : msgHandlerArgsNames) { //add it's parent's msgHandlers Args to the lists
                            if(str1.split("#", 2)[0].equals(now.split("#", 2)[1])) {
                                msgHandlerArgsNamesTemp.add(man +
                                        "#" + str1.split("#", 2)[1]);
                            }
                        }
                        for (String str1 : msgHandlerArgsNamesTemp) {
                            msgHandlerArgsNames.add(str1);
                        }
                        msgHandlerArgsNamesTemp.clear();
                        for (String str1 : msgHandlerLocalVarsNames) { //add it's parent's msgHandlers localVars to the lists
                            if(str1.split("#", 2)[0].equals(now.split("#", 2)[1])) {
                                msgHandlerLocalVarsNamesTemp.add(man +
                                        "#" + str1.split("#", 2)[1]);
                            }
                        }
                        for (String str1 : msgHandlerLocalVarsNamesTemp) {
                            msgHandlerLocalVarsNames.add(str1);
                        }
                        msgHandlerLocalVarsNamesTemp.clear();
                        if(now.split("#", 2)[1].equals("$noParent"))
                            break;
                        for (String s : parents) {
                            if(s.split("#", 2)[0].equals(now.split("#", 2)[1])) {
                                now = s;
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("^^^^^");
        for (String str : actorVarNames) {
            System.out.println(str);
        }
        System.out.println("^^^^^");
    }

    @Override
    public void visit(Program program) {
        firstOfAll(program);
        for (ActorDeclaration actor: program.getActors()) {
            currentActor = actor.getName().getName();
            actor.accept(this);
            actorNames.add(actor.getName().getName());
        }
        program.getMain().accept(this);
        for (String str : errorMessages) {
            System.out.println(str);
        }
    }

    @Override
    public void visit(ActorDeclaration actorDeclaration) {
        if(actorDeclaration.getParentName() != null) {
            if(!(actorNames.contains(actorDeclaration.getParentName().getName()))) {
                errorMessages.add(actorDeclaration.getParentName().getLine() + "#actor "
                        + actorDeclaration.getParentName().getName() + " is not declared");
            }
        }
        if(actorDeclaration.getInitHandler() != null) { //////////////////////////////////////
            currentHandler = actorDeclaration.getInitHandler().getName().getName();
            actorDeclaration.getInitHandler().accept(this);
            currentHandler = "";
        }
        for (HandlerDeclaration msgHandler : actorDeclaration.getMsgHandlers()) {
            currentHandler = msgHandler.getName().getName();
            msgHandler.accept(this);
            currentHandler = "";
        }
    }

    @Override
    public void visit(HandlerDeclaration handlerDeclaration) {
        for (Statement body : handlerDeclaration.getBody()) {
            body.accept(this);
        }
    }

    @Override
    public void visit(VarDeclaration varDeclaration) {
        varDeclarationNames.add(varDeclaration.getIdentifier().getName() + "#"
                + varDeclaration.getType().toString() + "#" + globalThisBlock);
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
        unaryExpression.getOperand().accept(this);
        if(unaryExpression.getUnaryOperator() == UnaryOperator.minus || unaryExpression.getUnaryOperator() == UnaryOperator.postdec
                || unaryExpression.getUnaryOperator() == UnaryOperator.postinc || unaryExpression.getUnaryOperator() == UnaryOperator.predec
                || unaryExpression.getUnaryOperator() == UnaryOperator.preinc) {
            if(!(unaryExpression.getOperand().getType().toString().equals("int"))) {
                errorMessages.add(unaryExpression.getLine() + "#unsupported operand type for "
                        + unaryExpression.getUnaryOperator().toString());
                unaryExpression.setType(new NoType());
            }
            else {
                unaryExpression.setType(new IntType());
            }
        }
        if(unaryExpression.getUnaryOperator() == UnaryOperator.not) {
            if(!(unaryExpression.getOperand().getType().toString().equals("boolean"))) {
                errorMessages.add(unaryExpression.getLine() + "#unsupported operand type for "
                        + unaryExpression.getUnaryOperator().toString());
                unaryExpression.setType(new NoType());
            }
            else {
                unaryExpression.setType(new BooleanType());
            }
        }
    }

    @Override
    public void visit(BinaryExpression binaryExpression) {
        binaryExpression.getLeft().accept(this);
        binaryExpression.getRight().accept(this);
        if(binaryExpression.getBinaryOperator() == BinaryOperator.eq || binaryExpression.getBinaryOperator() == BinaryOperator.neq) {
            binaryExpression.setType(new BooleanType());
        }
        if(binaryExpression.getBinaryOperator() == BinaryOperator.assign) {
            if(!isSubtype(binaryExpression.getRight().getType(), binaryExpression.getRight().getType())) {
                errorMessages.add(binaryExpression.getLine() + "#unsupported operand type for " + BinaryOperator.assign.toString());
                binaryExpression.setType(new NoType());
            }
            else {
                binaryExpression.setType(binaryExpression.getLeft().getType());
            }
        }
        if(binaryExpression.getBinaryOperator() == BinaryOperator.and || binaryExpression.getBinaryOperator() == BinaryOperator.or) {
            if(!(binaryExpression.getRight().getType().toString().equals("boolean"))
                    || !(binaryExpression.getLeft().getType().toString().equals("boolean"))) {
                errorMessages.add(binaryExpression.getLine() + "#unsupported operand type for "
                        + binaryExpression.getBinaryOperator().toString());
                binaryExpression.setType(new NoType());
            }
            else {
                binaryExpression.setType(new BooleanType());
            }
        }
        if(binaryExpression.getBinaryOperator() == BinaryOperator.mult || binaryExpression.getBinaryOperator() == BinaryOperator.div
                || binaryExpression.getBinaryOperator() == BinaryOperator.add || binaryExpression.getBinaryOperator() == BinaryOperator.sub
                || binaryExpression.getBinaryOperator() == BinaryOperator.mod || binaryExpression.getBinaryOperator() == BinaryOperator.gt
                || binaryExpression.getBinaryOperator() == BinaryOperator.lt) {
            if (!(binaryExpression.getRight().getType().toString().equals("int")
                    && binaryExpression.getLeft().getType().toString().equals("int"))) {
                errorMessages.add(binaryExpression.getLine() + "#unsupported operand type for "
                        + binaryExpression.getBinaryOperator().toString());
                binaryExpression.setType(new NoType());
            } else {
                binaryExpression.setType(new IntType());
            }
        }
    }

    @Override
    public void visit(ActorVarAccess actorVarAccess) {
        //TODO: implement appropriate visit functionality
    }

    @Override
    public void visit(Identifier identifier) {
        System.out.println("::::::::::::::::");
        System.out.println(currentActor);
        System.out.println(identifier.getName());
        boolean foundIt = false;
        //knownActorNames
        //actorVarNames
        //msgHandlerArgsNames
        //msgHandlerLocalVarsNames = new ArrayList<String>();
        //varDeclarationNames = new ArrayList<String>();
        for (String str : knownActorNames) {
            if(str.split("#", 3)[1].equals(identifier.getName()) && str.split("#", 3)[0].equals(currentActor)){
                identifier.setType(returnType(str.split("#", 3)[2], identifier));
                foundIt = true;
            }
        }
        for (String str : actorVarNames) {
            if(str.split("#", 3)[1].equals(identifier.getName()) && str.split("#", 3)[0].equals(currentActor)){
                identifier.setType(returnType(str.split("#", 3)[2], identifier));
                foundIt = true;
            }
        }
        for (String str : msgHandlerArgsNames2) {
            if (str.split("#", 4)[1].equals(identifier.getName()) && str.split("#", 4)[0].equals(currentHandler)
                    &&str.split("#", 4)[2].equals(currentActor)) {
                identifier.setType(returnType(str.split("#", 4)[3], identifier));
                foundIt = true;
            }
        }
        for (String str : msgHandlerLocalVarsNames) {
            if (str.split("#", 4)[1].equals(identifier.getName()) && str.split("#", 4)[0].equals(currentHandler)
                    &&str.split("#", 4)[3].equals(currentActor)) {
                identifier.setType(returnType(str.split("#", 4)[2], identifier));
                foundIt = true;
            }
        }
        if(!foundIt) {
            errorMessages.add(identifier.getLine() + "#variable " + identifier.getName() +" is not declared");
            identifier.setType(new NoType());
        }
    }

    @Override
    public void visit(Block block) {
        blockCounter ++;
        int thisBlock = blockCounter;
        globalThisBlock = thisBlock;
        blocks.add("Block#" + blockCounter);
        for (Statement S : block.getStatements()) {
            S.accept(this);
        }
        blocks.add("End#" + thisBlock);
        globalThisBlock --;
    }

    @Override
    public void visit(Conditional conditional) {
        if(conditional.getExpression() != null) {
            conditional.getExpression().accept(this);
            if (!(conditional.getExpression().getType().toString().equals("boolean"))) {
                errorMessages.add(conditional.getExpression().getLine() + "#" + "condition type must be Boolean");
            }
            conditional.getExpression().accept(this);
        }
        if(conditional.getThenBody() != null) {
            conditional.getThenBody().accept(this);
        }
        if(conditional.getElseBody() != null) {
            conditional.getElseBody().accept(this);
        }
    }

    @Override
    public void visit(For loop) {
        if(loop.getInitialize() != null) {
            loop.getInitialize().getlValue().accept(this);
            loop.getInitialize().getlValue().accept(this);
        }
        if(loop.getUpdate() != null) {
            loop.getUpdate().getlValue().accept(this);
            loop.getUpdate().getlValue().accept(this);
        }
        if(loop.getCondition() != null) {
            loop.getCondition().accept(this);
            if (!(loop.getCondition().getType().toString().equals("boolean"))) {
                errorMessages.add(loop.getCondition().getLine() + "#condition type must be Boolean");
            }
            loop.getCondition().accept(this);
        }
    }

    @Override
    public void visit(MsgHandlerCall msgHandlerCall) {
        if(msgHandlerCall.getInstance() instanceof Self) {
            boolean foundIt = false;
            for (String str : msgHandlerNames) {
                if(str.split("#", 2)[0].equals(currentActor)
                        && str.split("#", 2)[1].equals(msgHandlerCall.getMsgHandlerName().getName())) {
                    foundIt = true;
                }
            }
            if(!foundIt) {
                errorMessages.add(msgHandlerCall.getMsgHandlerName().getLine() + "#"
                        + "there is no msghandler name " + msgHandlerCall.getMsgHandlerName().getName()
                        + " in actor " + currentActor);
            }
        }
        else if(msgHandlerCall.getInstance()  instanceof Sender) {
            if (msgHandlerCall.getMsgHandlerName().getName().equals("initial")) {
                errorMessages.add(msgHandlerCall.getMsgHandlerName().getLine() + "#" + "no sender in initial msghandler");
            }
        }
        else if(msgHandlerCall.getInstance() instanceof Identifier){
            if(!(actorNames.contains(((Identifier) msgHandlerCall.getInstance()).getName()))) {
                boolean flag = false;
                for (String str : knownActorNames) {
                    if(
                            str.split("#", 3)[1].equals(((Identifier) msgHandlerCall.getInstance()).getName())
                            && str.split("#", 3)[0].equals(currentActor)) {
                        flag = true;
                    }
//                        actorDeclaration.getName().getName() + "#" +
//                                knownActor.getIdentifier().getName() + "#" + knownActor.getType().toString()
                }
                if(!flag) {
                    errorMessages.add(msgHandlerCall.getInstance().getLine() + "#actor "
                            + ((Identifier) msgHandlerCall.getInstance()).getName() + " is not declared"); //// Instance name ??
                    return;
                }
            }
            if(!(msgHandlerNames.contains(((Identifier) msgHandlerCall.getInstance()).getName() + "#" +
                    msgHandlerCall.getMsgHandlerName().getName()))) {
                boolean flag = false;
                for (String str : knownActorNames) {
                    if(str.split("#", 3)[1].equals(((Identifier) msgHandlerCall.getMsgHandlerName()).getName())
                                    && str.split("#", 3)[0].equals(currentActor)) {
                        flag = true;
                    }
//                        actorDeclaration.getName().getName() + "#" +
//                                knownActor.getIdentifier().getName() + "#" + knownActor.getType().toString()
                }
                if(!flag) {
                    errorMessages.add(msgHandlerCall.getMsgHandlerName().getLine()
                            + "#there is no msghandler name " + msgHandlerCall.getMsgHandlerName().getName()
                            + " in actor " + ((Identifier) msgHandlerCall.getInstance()).getName());
                    return;
                }
            }
            else {
                boolean flag = false;
                for (int i = 0; i < msgHandlerArgsNames.size(); i++) {
                    String argName = msgHandlerArgsNames.get(i);
                    if(msgHandlerCall.getMsgHandlerName().getName().equals(argName.split("#", 2)[0])
                            && ((Identifier) msgHandlerCall.getInstance()).getName().equals(argName.split("#", 2)[2])) {
                        int z = 0;
                        flag = true;
                        for (Expression exp : msgHandlerCall.getArgs()) {
                            argName = msgHandlerArgsNames.get(i+z);
                            if(msgHandlerCall.getMsgHandlerName().getName().equals(argName.split("#", 2)[0])
                                    && ((Identifier) msgHandlerCall.getInstance()).getName().equals(argName.split("#", 2)[2])) {
                                exp.accept(this);
                                if (!(exp.getType().toString().equals(argName.split("#", 3)[1]))) {
                                    errorMessages.add(exp.getLine() + "#" + "knownactors does not match with definition");
                                }
                            }
                            else {
                                break;
                            }
                            z++;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void visit(Print print) {

    }

    @Override
    public void visit(Assign assign) {
        assign.getlValue().accept(this);
        assign.getrValue().accept(this);
        if(!(isSubtype(assign.getrValue().getType(), assign.getlValue().getType()))) {
            errorMessages.add(assign.getLine() + "#left side of assignment must be a valid lvalue");
            assign.getlValue().setType(new NoType());
        }
        else {
            System.out.println("MAHYA");
        }
    }

    @Override
    public void visit(BooleanValue value) {
        value.setType(new BooleanType());
    }

    @Override
    public void visit(IntValue value) {
        value.setType(new IntType());
    }

    @Override
    public void visit(StringValue value) {
        value.setType(new StringType());
    }

    @Override
    public void visit(ArrayCall arrayCall) {
        arrayCall.setType(new ArrayType(1));
    }

    @Override
    public void visit(Self self) {
        return;
    }

    @Override
    public void visit(Sender sender) {
        return;
    }

    @Override
    public void visit(Break breakLoop) {
        return;
    }

    @Override
    public void visit(Continue continueLoop) {
        return;
    }
}