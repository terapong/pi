package toto.pi3.ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MushRoom implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private BigDecimal temperatureMin;
	private BigDecimal temperatureMax;
	private BigDecimal humidityMin;
	private BigDecimal humidityMax;
	private BigDecimal co2Min;
	private BigDecimal co2Max;
	private Date updateDate;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTemperatureMin() {
		return temperatureMin;
	}

	public void setTemperatureMin(BigDecimal temperatureMin) {
		this.temperatureMin = temperatureMin;
	}

	public BigDecimal getTemperatureMax() {
		return temperatureMax;
	}

	public void setTemperatureMax(BigDecimal temperatureMax) {
		this.temperatureMax = temperatureMax;
	}

	public BigDecimal getHumidityMin() {
		return humidityMin;
	}

	public void setHumidityMin(BigDecimal humidityMin) {
		this.humidityMin = humidityMin;
	}

	public BigDecimal getHumidityMax() {
		return humidityMax;
	}

	public void setHumidityMax(BigDecimal humidityMax) {
		this.humidityMax = humidityMax;
	}

	public BigDecimal getCo2Min() {
		return co2Min;
	}

	public void setCo2Min(BigDecimal co2Min) {
		this.co2Min = co2Min;
	}

	public BigDecimal getCo2Max() {
		return co2Max;
	}

	public void setCo2Max(BigDecimal co2Max) {
		this.co2Max = co2Max;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
