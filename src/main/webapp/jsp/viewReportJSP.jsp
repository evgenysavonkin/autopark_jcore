<%@ page import="java.util.Set" %>
<%@ page import="org.evgenysav.infrastructure.dto.classes_dto.VehicleDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.concurrent.atomic.AtomicReference" %>
<%@ page import="java.util.function.Predicate" %>
<%@ page import="java.util.Optional" %>
<%@ page import="org.evgenysav.infrastructure.dto.classes_dto.RentDto" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Автомобили</title>
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
</head>
<body>
<div class="center flex full-vh">
    <div class="vertical-center">
        <%
            List<VehicleDto> dtoList = (List<VehicleDto>) request.getAttribute("cars");
            List<RentDto> rents = (List<RentDto>) request.getAttribute("rents");
        %>

        <a class="ml-20" href="/">На главную</a>
        <br/>
        <br/>
        <hr/>
        <br/>
        <table>
            <caption>Автомобили</caption>
            <tr>
                <th>Тип</th>
                <th>Модель</th>
                <th>Регистрационный номер</th>
                <th>Масса</th>
                <th>Дата выпуска</th>
                <th>Цвет</th>
                <th>Тип двигателя</th>
                <th>Пробег</th>
                <th>Доход с аренд</th>
                <th>Налог</th>
                <th>Итог</th>
            </tr>

            <%if (dtoList.size() == 0) {%>
            <tr>
                <td colspan="10">
                    Авто с заданными параметрами не найдены
                </td>
            </tr>
            <%}%>
            <%
                for (VehicleDto dto : dtoList) {
            %>
            <tr>
                <td><%=dto.getTypeName()%>
                </td>
                <td><%=dto.getModelName()%>
                </td>

                <td><%=dto.getRegistrationNumber()%>
                </td>

                <td><%=dto.getWeight()%>
                </td>

                <td><%=dto.getManufactureYear()%>
                </td>

                <td><%=dto.getColor()%>
                </td>
                <td><%=dto.getEngineName()%>
                </td>

                <td><%=dto.getMileage()%>
                </td>

                <td><%=
                rents.stream()
                        .filter(rent -> rent.getVehicleId() == dto.getVehicleId())
                        .mapToDouble(RentDto::getRentalCost)
                        .sum()
                %>
                </td>
                <td>
                    <%=
                    (dto.getWeight() * 0.0013) + (dto.getTaxCoefficient() * dto.getEngineTaxCoefficient() * 30) + 5
                    %>
                </td>
                <td>
                    <%=
                    rents.stream()
                            .filter(rent -> rent.getVehicleId() == dto.getVehicleId())
                            .mapToDouble(RentDto::getRentalCost)
                            .sum() - (dto.getWeight() * 0.0013) + (dto.getTaxCoefficient() * dto.getEngineTaxCoefficient() * 30) + 5
                    %>
                </td>
            </tr>
            <%}%>
        </table>
        <br/>
        <div>
            <hr/>
            <br/>
            <p>Средний налог за месяц:
                <strong><%=
                dtoList.stream()
                        .mapToDouble(dto -> {
                            return (dto.getWeight() * 0.0013) + (dto.getTaxCoefficient() * dto.getEngineTaxCoefficient() * 30) + 5;
                        })
                        .average()
                        .getAsDouble()
                %>
                </strong>
            </p>
            <p>Средний доход за месяц:
                <strong><%=
                rents.stream()
                        .mapToDouble(RentDto::getRentalCost)
                        .average().getAsDouble()
                %>
                </strong>
            </p>
            <p>Доход автопарка:
                <strong><%=

                rents.stream()
                        .mapToDouble(RentDto::getRentalCost)
                        .sum() - dtoList.stream()
                        .mapToDouble(dto -> {
                            return (dto.getWeight() * 0.0013) + (dto.getTaxCoefficient() * dto.getEngineTaxCoefficient() * 30) + 5;
                        })
                        .sum()
                %>
                </strong>
            </p>
            <br/>
            <hr/>
        </div>
    </div>
</div>
</body>
</html>