package com.epam.esm.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tag.class)
public abstract class Tag_ {

	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, Long> id;
	public static volatile SetAttribute<Tag, GiftCertificate> giftCertificates;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String GIFT_CERTIFICATES = "giftCertificates";

}

