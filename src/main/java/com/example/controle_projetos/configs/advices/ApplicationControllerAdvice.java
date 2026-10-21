package com.example.controle_projetos.configs.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.controle_projetos.configs.exceptions.RegraNegocioException;
import com.example.controle_projetos.dtos.ApiErrorDTO;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationControllerAdvice {
    @ExceptionHandler(RegraNegocioException.class)
    public ApiErrorDTO handleRegraNegocioException(
            RegraNegocioException ex) {
        String msg = ex.getMessage();
        return new ApiErrorDTO(msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorDTO handleJsonInvalido(HttpMessageNotReadableException ex) {
        return new ApiErrorDTO(
                "Corpo da requisição inválido. Verifique o JSON e o formato das datas (AAAA-MM-DD)");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiErrorDTO handleParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return new ApiErrorDTO("Valor inválido para o parâmetro '" + ex.getName() + "'");
    }

}
