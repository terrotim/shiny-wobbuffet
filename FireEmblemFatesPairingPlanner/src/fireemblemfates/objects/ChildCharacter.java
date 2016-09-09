package fireemblemfates.objects;

import java.util.HashSet;

public class ChildCharacter extends FirstGenCharacter{
	String constParent = new String();
	String varParent = new String();
	double[] baseGrowthRates = new double[8];
	
	public ChildCharacter(String name,String constParent,double[] baseGrowthRates,HashSet<String> supportSet, String startClass){
		this.setName(name);
		this.setConstParent(constParent);
		this.setBaseGrowthRates(baseGrowthRates);
		this.setSupportSet(supportSet);
		this.setClassSet(new String[]{startClass,"","","",""});
	}

	public String getConstParent() {
		return constParent;
	}

	public void setConstParent(String constParent) {
		this.constParent = constParent;
	}

	public String getVarParent() {
		return varParent;
	}

	public void setVarParent(String varParent) {
		this.varParent = varParent;
	}

	public double[] getBaseGrowthRates() {
		return baseGrowthRates;
	}

	public void setBaseGrowthRates(double[] baseGrowthRates) {
		this.baseGrowthRates = baseGrowthRates;
	}
	
//	public ChildCharacter(String name, double[] growthRates, String[] classSet, HashSet<String> supportSet,
//			String constParent, String varParent) {
//		super(name, growthRates,statModifiers,pairupStats,classSet,supportSet);
//
//	}
}
