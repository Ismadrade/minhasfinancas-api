package com.dsousa.minhasfinancas;

import java.util.Calendar;
import java.util.Scanner;

	public class Exercicio {

			//ano
			public static final int ano_atual = Calendar.getInstance().get(Calendar.YEAR);

			//mes
			public static final int mes_atual = Calendar.getInstance().get(Calendar.MONTH)+1;
				
			//dia
			public static final int dia_atual = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


		public static void main(String[] args) {
			
			//Entrada do Usuario
			Scanner sc = new Scanner(System.in);
			int dia, mes, ano;
			System.out.println("Qual o dia do seu nascimento: ");
			dia = Integer.parseInt(sc.nextLine());
			System.out.println("Qual o mês do seu nascimento: ");
			mes = Integer.parseInt(sc.nextLine());
			System.out.println("Qual o ano do seu nascimento: ");
			ano = Integer.parseInt(sc.nextLine());
			
			int idade = ano_atual - ano;
					
			//Pegando a idade exata da pessoa
			
			if(mes > mes_atual || (mes == mes_atual && dia > dia_atual)) {
				idade --;
			}		
			
			//diminui o mes de aniversário com o mes atual, pra obter
			mes = mes - mes_atual;
			
			if(mes  < 0 ){
				mes *= -1;
			}
			
			if(ano == ano_atual || mes == 0) {
				mes *=30; 
			}else {
			
			mes = (12 - mes)*30;
			}
			
			
			dia = dia - dia_atual;
			if(dia  < 0 ){
				dia *= -1;
			}
			
			
			idade = (idade *365) + mes + dia;
			
			System.out.println("Sua idade em dias é: " + idade);
			
			sc.close();
			
			
		}

	}