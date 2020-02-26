create sequence pricing_policy_seq;
create table pricing_policy ( pp_id number default pricing_policy_seq.nextval primary key,
pricing_policy_name varchar(50) ,
is_hourly boolean(1),
fixed_price decimal(5,2),
hour_price decimal(5,2),
cleaning_service_charge decimal(5,2),
other_extra_charges decimal (5,2)
);
ALTER TABLE
    pricing_policy
    ADD CONSTRAINT FailedValidationFor_HourPriceCanNotBeZeroWhenItIsFixed
        check (
                ( is_hourly is true and hour_price > 0 )
                or
                ( is_hourly is false )
            );


create sequence parking_slot_seq;
create table parking_slot ( ps_id number default parking_slot_seq.nextval primary key,
                            parking_type varchar(25) ,
                            is_available boolean(1),
                            pricing_policy_id number,
                            foreign key (pricing_policy_id) references pricing_policy(pp_id)
);

create sequence parking_billing_seq;
create table parking_billing ( pb_id number default parking_billing_seq.nextval primary key,
                            vehicle_number varchar(25),
                            start_time TIMESTAMP,
                            end_time TIMESTAMP,
                            is_billed boolean(1),
                            bill_amount decimal(5,2),
                            parking_slot_id number,
                            foreign key (parking_slot_id) references parking_slot(ps_id)
);

insert into pricing_policy(pricing_policy_name,is_hourly,fixed_price,hour_price,cleaning_service_charge,other_extra_charges) values('Hourly-STANDARD',true,12.0,15.0,0.0,0.0);
insert into pricing_policy(pricing_policy_name,is_hourly,fixed_price,hour_price,cleaning_service_charge,other_extra_charges) values('Fixed+Hourly-STANDARD',false,10.0,8.0,0.0,0.0);
insert into pricing_policy(pricing_policy_name,is_hourly,fixed_price,hour_price,cleaning_service_charge,other_extra_charges) values('Hourly-CAR_20KW_ELECTRIC',true,15.0,18.0,0.0,0.0);
insert into pricing_policy(pricing_policy_name,is_hourly,fixed_price,hour_price,cleaning_service_charge,other_extra_charges) values('Fixed+Hourly-CAR_20KW_ELECTRIC',false,18.0,16.0,0.0,0.0);
insert into pricing_policy(pricing_policy_name,is_hourly,fixed_price,hour_price,cleaning_service_charge,other_extra_charges) values('Hourly-CAR_50KW_ELECTRIC',true,18.0,20.0,0.0,0.0);
insert into pricing_policy(pricing_policy_name,is_hourly,fixed_price,hour_price,cleaning_service_charge,other_extra_charges) values('Fixed+Hourly-CAR_50KW_ELECTRIC',false,20.0,15.0,0.0,0.0);


insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,1);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,1);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,1);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,1);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,1);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,2);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,2);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,2);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,2);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('STANDARD',true,2);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_20KW_ELECTRIC',true,3);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_20KW_ELECTRIC',true,3);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_20KW_ELECTRIC',true,3);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_20KW_ELECTRIC',true,4);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_20KW_ELECTRIC',true,4);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_20KW_ELECTRIC',true,4);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_50KW_ELECTRIC',true,5);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_50KW_ELECTRIC',true,5);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_50KW_ELECTRIC',true,5);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_50KW_ELECTRIC',true,6);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_50KW_ELECTRIC',true,6);
insert into parking_slot (parking_type,is_available,pricing_policy_id) values ('CAR_50KW_ELECTRIC',true,6);