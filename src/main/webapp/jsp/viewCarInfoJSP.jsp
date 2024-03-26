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
                <th>Расход</th>
                <th>Коэффициент налога</th>
                <th>км на полный бак</th>
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

                <td>
                    <%=dto.getPer100km()%>
                </td>
                <td>
                    <%=dto.getTaxCoefficient()%>
                </td>
                <td>
                    <%=
                    dto.getMaxKm()
                    %>
                </td>
            </tr>
            <%}%>
        </table>
        <br/>
        <p>Налог за месяц:

            <strong>
                <%
                    VehicleDto firstDto = dtoList.getFirst();
                %>

                <%=
                (firstDto.getWeight() * 0.0013) + (firstDto.getTaxCoefficient() * firstDto.getEngineTaxCoefficient() * 30) + 5
                %>
            </strong>

        </p>
    </div>
</div>
</body>
</html>