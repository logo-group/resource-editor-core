package com.lbs.re.model.languages;

import com.lbs.re.model.ReLanguageTable;
import com.lbs.re.model.ReResourceitem;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RE_GEORGIANGE", indexes = { @Index(name = "I_GEORGIANGE_INFO", columnList = "INFO,ID", unique = true),
		@Index(name = "I_GEORGIANGE_RESITEMREF", columnList = "RESOURCEITEMREF", unique = false) })
public class ReGeorgiange extends ReLanguageTable {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCEITEMREF", referencedColumnName = "ID", insertable = false, updatable = false)
	public ReResourceitem reResourceitem;

	public ReGeorgiange() {
		/* */
	}

	public ReGeorgiange cloneGeorgiange(ReResourceitem item) {
		ReGeorgiange georgiange = new ReGeorgiange();
		georgiange = cloneLanguage(item, georgiange);
		return georgiange;
	}
}
